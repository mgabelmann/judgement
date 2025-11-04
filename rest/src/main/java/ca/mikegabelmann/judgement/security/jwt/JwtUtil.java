package ca.mikegabelmann.judgement.security.jwt;

import ca.mikegabelmann.judgement.JudgementUtil;
import ca.mikegabelmann.judgement.config.JudgementConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    private static final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.refreshtoken.salt}")
    private String refreshTokenSalt;

    /** expiration of access token - default 1 hour. */
    @Value("${jwt.access.expiration:3600000}")
    private int jwtAccessExpiration;

    /** expiration of refresh token - default 1 day. */
    @Value("${jwt.refresh.expiration:84000000}")
    private int jwtRefreshExpiration;

    /** secret key used for encrypting JWT token. */
    private volatile SecretKey secretKey;

    private final JudgementConfiguration judgementConfiguration;


    @Autowired
    public JwtUtil(final JudgementConfiguration judgementConfiguration) {
        this.judgementConfiguration = judgementConfiguration;
    }

    @PostConstruct
    public void postConstruct() {
        this.ensureVariableSet("jwt.secret", jwtSecret, "local");
        this.ensureVariableSet("jwt.refreshtoken.salt", refreshTokenSalt, "local");

        //set secret key for JWT
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     *
     * @param username
     * @param authorities
     * @return
     */
    public String generateAccessToken(final String username, final Collection<? extends GrantedAuthority> authorities) {
        return this.generateToken(username, authorities, jwtAccessExpiration, secretKey, ALGORITHM);
    }

    /**
     *
     * @return
     */
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    /**
     *
     * @return
     */
    public Instant getRefreshTokenExpiration() {
        return Instant.now().plusMillis(jwtRefreshExpiration);
    }

    /**
     *
     * @param token
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String hashRefreshToken(final String token) throws NoSuchAlgorithmException {
        String salt = this.refreshTokenSalt;
        byte[] tokenToHash = (salt + token).getBytes(StandardCharsets.UTF_8);

        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hash = digest.digest(tokenToHash);

        return JudgementUtil.bytesToHex(hash);
    }

    /**
     *
     * @param username
     * @param authorities
     * @param expiry
     * @param secretKey
     * @param algorithm
     * @return
     */
    public String generateToken(final String username, Collection<? extends GrantedAuthority> authorities, final int expiry, final SecretKey secretKey, final MacAlgorithm algorithm) {
        LOGGER.debug("generate JWT token for username {}", username);

        String roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusMillis(expiry)))
                .signWith(secretKey, algorithm)
                .claim("roles", roles)
                .compact();
    }

    /**
     *
     * @param token
     * @return
     */
    public Collection<? extends GrantedAuthority> getRolesFromToken(final String token) {
        Jws<Claims> claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        String roleStr =  claims.getPayload().get("roles").toString();
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(roleStr.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        LOGGER.trace("found {} roles in token", authorities.size());

        return authorities;
    }

    /**
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken(final String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getSubject();
    }

    /**
     *
     * @param token
     * @return
     */
    public boolean validateToken(final String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;

        } catch (final io.jsonwebtoken.security.SecurityException e) {
            LOGGER.debug("invalid JWT signature : {}", e.getMessage());

        } catch (final MalformedJwtException e) {
            LOGGER.debug("invalid JWT token : {}", e.getMessage());

        } catch (final ExpiredJwtException e) {
            LOGGER.debug("expired JWT token : {}", e.getMessage());

        } catch (final UnsupportedJwtException e) {
            LOGGER.debug("unsupported JWT token : {}", e.getMessage());

        } catch (final IllegalArgumentException e) {
            LOGGER.debug("invalid JWT claims : {}", e.getMessage());
        }

        return false;
    }

    /**
     * Helper method to ensure that a Spring property is set and not empty or null.
     * @param propertyName Spring config property name
     * @param value variable
     * @param profile which profile is
     * @throws NullPointerException value is null or empty
     */
    void ensureVariableSet(final String propertyName, final String value, final String profile) {
        if (value == null || value.isEmpty()) {
            //variable must have a value
            throw new NullPointerException(propertyName + " can not be null or empty. You MUST set this value, preferably as an environment variable.");

        } else if (judgementConfiguration.isProfileActive(profile)) {
            LOGGER.warn("{}={}. NOTE: only displayed when using '{}' profile", propertyName, value, profile);

        } else {
            LOGGER.info("{} has been successfully set", propertyName);
        }
    }

}
