package ca.mikegabelmann.judgement.security.jwt;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    /** expiration of access token - default 1 hour. */
    @Value("${jwt.access.expiration:3600000}")
    private int jwtAccessExpiration;

    /** expiration of refresh token - default 1 day. */
    @Value("${jwt.refresh.expiration:84000000}")
    private int jwtRefreshExpiration;

    /** secret key used for encrypting JWT token. */
    private SecretKey secretKey;

    private final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;


    public JwtUtil() {}

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        LOGGER.debug("JWT secret key is set");
    }

    public String generateAccessToken(final String username) {
        return this.generateToken(username, jwtAccessExpiration, secretKey, ALGORITHM);
    }

    public String generateRefreshToken(final String username) {
        return this.generateToken(username, jwtRefreshExpiration, secretKey, ALGORITHM);
    }

    public String generateToken(final String username, final int expiry, final SecretKey secretKey, final MacAlgorithm algorithm) {
        LOGGER.debug("generate JWT token for username {}", username);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusMillis(expiry)))
                .signWith(secretKey, algorithm)
                .compact();
    }

    public String generateAccessToken(final String username, final Collection<? extends GrantedAuthority> authorities) {
        return this.generateToken(username, authorities, jwtAccessExpiration, secretKey, ALGORITHM);
    }

    public String generateRefreshToken(final String username, final Collection<? extends GrantedAuthority> authorities) {
        return this.generateToken(username, authorities, jwtRefreshExpiration, secretKey, ALGORITHM);
    }

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

    public Collection<? extends GrantedAuthority> parseRolesFromToken(final String token) {
        Jws<Claims> claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        String roleStr =  claims.getPayload().get("roles").toString();
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(roleStr.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        LOGGER.trace("found {} roles in token", authorities.size());

        return authorities;
    }

    public String getUsernameFromToken(final String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getSubject();
    }

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

}
