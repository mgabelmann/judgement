package ca.mikegabelmann.judgement.controller.rest;

import ca.mikegabelmann.judgement.persistence.model.Account;
import ca.mikegabelmann.judgement.persistence.model.RefreshToken;
import ca.mikegabelmann.judgement.persistence.repository.RefreshTokenRepository;
import ca.mikegabelmann.judgement.persistence.service.AccountActivityLogService;
import ca.mikegabelmann.judgement.security.auth.JudgementAuthenticationProvider;
import ca.mikegabelmann.judgement.security.auth.JudgementUserDetailsService;
import ca.mikegabelmann.judgement.security.jwt.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Optional;


@RestController
public class AuthRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRestController.class);

    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    //private final AuthenticationProvider authenticationProvider;
    private final JudgementAuthenticationProvider authenticationProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final JudgementUserDetailsService judgementUserDetailsService;
    private final AccountActivityLogService accountActivityLogService;

    @Autowired
    public AuthRestController(
            //final AuthenticationProvider authenticationProvider,
            final JudgementAuthenticationProvider authenticationProvider,
            final SecurityContextRepository securityContextRepository,
            final RefreshTokenRepository refreshTokenRepository,
            final JwtUtil jwtUtil, JudgementUserDetailsService judgementUserDetailsService, AccountActivityLogService accountActivityLogService) {

        this.securityContextRepository = securityContextRepository;
        this.authenticationProvider = authenticationProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.judgementUserDetailsService = judgementUserDetailsService;
        this.accountActivityLogService = accountActivityLogService;
    }

/*
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());

        Authentication authResponse = authenticationProvider.authenticate(authRequest);

        //NOTE: this auth provider prefixes the users password with a random salt that is then stored in the DB and reused on reauthentication
        //Authentication authResponse = judgementAuthenticationProviderServiceImpl.authenticate(authRequest);

        //persist our authentication
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authResponse);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        return new ResponseEntity<>("user authenticated", HttpStatus.OK);
    }
*/

    //curl -v http://localhost:8080/login -H "cache-control: no-cache" -H "content-type: application/json" -d "{\"username\":\"ADMIN\",\"password\":\"123456\"}"
    @PostMapping(path = "/jwtlogin")
    public ResponseEntity<?> jwtLogin(@RequestBody LoginRequest loginRequest) throws NoSuchAlgorithmException {
        String username = loginRequest.getUsername();
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, loginRequest.getPassword());

        Authentication authResponse = authenticationProvider.authenticate(authRequest);

        //TODO: move this to a token service or refresh token service class?!?

        Optional<RefreshToken> tmpRefreshToken = refreshTokenRepository.findByUsername(username);
        if (tmpRefreshToken.isPresent()) {
            RefreshToken token = tmpRefreshToken.get();

            if (token.getExpiry().isBefore(Instant.now())) {
                //expired refresh token, delete refresh token and proceed
                LOGGER.debug("user={}: deleting expired refresh token",  username);
                refreshTokenRepository.delete(token);

            } else {
                //active, user already has a session, refuse
                LOGGER.debug("user={} already has an active session",  username);
                return ResponseEntity.badRequest().body("user already has an active session. logout");
            }
        }

        String accessToken = jwtUtil.generateAccessToken(username, authResponse.getAuthorities());

        String refreshToken = jwtUtil.generateRefreshToken().toString();
        String hashedToken = jwtUtil.hashRefreshToken(refreshToken);

        Instant refreshExpiry = jwtUtil.getRefreshTokenExpiration();
        Instant now = Instant.now();

        RefreshToken refreshTokenEntity = new RefreshToken(null, username, refreshExpiry, hashedToken, username, now, username, now, 0L);
        refreshTokenRepository.save(refreshTokenEntity);

        //save request for refresh token
        //accountActivityLogService.saveLog(account, "refresh token request - granted");

        return ResponseEntity.ok(new JwtTokenResponse(accessToken, refreshToken));
    }

    @PostMapping(path = "/jwtrefresh")
    public ResponseEntity<?> jwtRefresh(@RequestBody JwtRefreshRequest token) throws NoSuchAlgorithmException {
        String oldHashedToken = jwtUtil.hashRefreshToken(token.getRefresh());
        Optional<RefreshToken> oldToken = refreshTokenRepository.findByToken(oldHashedToken);

        if (oldToken.isPresent()) {
            RefreshToken currentToken = oldToken.get();
            String username = currentToken.getUsername();

            if (currentToken.getExpiry().isBefore(Instant.now())) {
                //expired, can't refresh
                LOGGER.debug("refresh requested, but token has expired. deleting refresh token");
                refreshTokenRepository.delete(currentToken);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            } else {
                //active, so refresh (happy path)
                Account account = judgementUserDetailsService.loadAccountByUsername(username);
                UserDetails userDetails = judgementUserDetailsService.getUserDetails(account);
                String accessToken = jwtUtil.generateAccessToken(username, userDetails.getAuthorities());

                String newToken = jwtUtil.generateRefreshToken().toString();
                String newHashedToken = jwtUtil.hashRefreshToken(newToken);

                Instant refreshExpiry = jwtUtil.getRefreshTokenExpiration();

                currentToken.setToken(newHashedToken);
                currentToken.setModifiedBy(username);
                currentToken.setModifiedOn(Instant.now());
                currentToken.setExpiry(refreshExpiry);
                refreshTokenRepository.save(currentToken);

                //save request for refresh token
                accountActivityLogService.saveLog(account, "refresh token request - granted");

                LOGGER.debug("refresh - issuing new access/refresh token");

                return ResponseEntity.ok(new JwtTokenResponse(accessToken, newToken));
            }

        } else {
            return ResponseEntity.badRequest().body("refresh token " + token.getRefresh() + " does not exist");
        }
    }

    //curl -v http://localhost:8080/testsecured -H "cache-control: no-cache" -H "content-type: application/json" -H "Authorization: Bearer <accesstoken>"
    @GetMapping("/testsecured")
    public ResponseEntity<String> testSecured() {
        return new ResponseEntity<>("<h1 style=\"color=#F00\">Secure end point</h1>", HttpStatus.OK);
    }

    @GetMapping("/loginsuccess")
    public ResponseEntity<String> loginsuccess() {
        return new ResponseEntity<>("<h1>login - success</h1>", HttpStatus.OK);
    }

    @GetMapping("/logoutsuccess")
    public ResponseEntity<String> logoutsuccess() {
        return new ResponseEntity<>("<h1>logout - success</h1>", HttpStatus.OK);
    }

    /**
     * Returned for login or refresh requests.
     */
    public static class JwtTokenResponse {
        public final String access;
        public final String refresh;

        public JwtTokenResponse(final String access, final String refresh) {
            this.access = access;
            this.refresh = refresh;
        }

        public String getAccess() {
            return access;
        }

        public String getRefresh() {
            return refresh;
        }
    }

    /**
     * Required for refresh requests.
     */
    public static class JwtRefreshRequest {
        public String refresh;

        protected JwtRefreshRequest() {}

        public JwtRefreshRequest(String refresh) {
            this.refresh = refresh;
        }

        public String getRefresh() {
            return refresh;
        }

        public void setRefresh(String refresh) {
            this.refresh = refresh;
        }
    }

    /**
     * Required for login requests.
     */
    public static class LoginRequest {
        private String username;
        private String password;

        protected LoginRequest() {}

        public LoginRequest(final String username, final String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
