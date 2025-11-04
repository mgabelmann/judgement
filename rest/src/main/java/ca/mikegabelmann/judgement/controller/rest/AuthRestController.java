package ca.mikegabelmann.judgement.controller.rest;

import ca.mikegabelmann.judgement.persistence.model.RefreshToken;
import ca.mikegabelmann.judgement.persistence.repository.RefreshTokenRepository;
import ca.mikegabelmann.judgement.persistence.service.AccountActivityLogService;
import ca.mikegabelmann.judgement.security.auth.JudgementAuthenticationProvider;
import ca.mikegabelmann.judgement.security.auth.JudgementUserDetailsService;
import ca.mikegabelmann.judgement.security.jwt.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final JudgementAuthenticationProvider authenticationProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JudgementUserDetailsService judgementUserDetailsService;
    private final AccountActivityLogService accountActivityLogService;
    private final JwtUtil jwtUtil;


    @Autowired
    public AuthRestController(
            //final AuthenticationProvider authenticationProvider,
            final JudgementAuthenticationProvider authenticationProvider,
            final RefreshTokenRepository refreshTokenRepository,
            final JwtUtil jwtUtil,
            final JudgementUserDetailsService judgementUserDetailsService,
            final AccountActivityLogService accountActivityLogService) {

        this.authenticationProvider = authenticationProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.judgementUserDetailsService = judgementUserDetailsService;
        this.accountActivityLogService = accountActivityLogService;
    }

    //curl -v http://localhost:8080/api/jwtlogin -H "cache-control: no-cache" -H "content-type: application/json" -d "{\"username\":\"ADMIN\",\"password\":\"123456\"}"
    @PostMapping(path = "/api/jwtlogin")
    public ResponseEntity<?> jwtLogin(@RequestBody JwtLoginRequest loginRequest) throws NoSuchAlgorithmException {
        String username = loginRequest.getUsername();

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, loginRequest.getPassword());
        Authentication authResponse = authenticationProvider.authenticate(authRequest);

        //TODO: move this to a token service or refresh token service class?!?

        Optional<RefreshToken> tmpRefreshToken = refreshTokenRepository.findByUsername(username);
        if (tmpRefreshToken.isPresent()) {
            RefreshToken token = tmpRefreshToken.get();

            if (token.getExpiry().isBefore(Instant.now())) {
                //expired refresh token, delete refresh token and proceed
                refreshTokenRepository.delete(token);

                LOGGER.debug("user={}: deleting expired refresh token",  username);
                accountActivityLogService.save(username, "login token request - expired, deleting refresh token");

            } else {
                //active, user already has a session, refuse
                LOGGER.debug("user={} already has an active session",  username);
                return ResponseEntity.badRequest().body("user already has an active session. logout");
            }
        }

        String accessToken = jwtUtil.generateAccessToken(username, authResponse.getAuthorities());

        String refreshToken = jwtUtil.generateRefreshToken();
        String hashedToken = jwtUtil.hashRefreshToken(refreshToken);

        Instant refreshExpiry = jwtUtil.getRefreshTokenExpiration();
        Instant now = Instant.now();

        RefreshToken refreshTokenEntity = new RefreshToken(null, username, refreshExpiry, hashedToken, username, now, username, now, 0L);
        refreshTokenRepository.save(refreshTokenEntity);

        //save request for refresh token
        accountActivityLogService.save(username, "login token request - granted");

        return ResponseEntity.ok(new JwtTokenResponse(accessToken, refreshToken));
    }

    //curl -v http://localhost:8080/api/jwtrefresh -H "cache-control: no-cache" -H "content-type: application/json" -d "{\"refresh\":\"<refreshtoken>\"}"
    @PostMapping(path = "/api/jwtrefresh")
    public ResponseEntity<?> jwtRefresh(@RequestBody JwtRefreshTokenRequest token) throws NoSuchAlgorithmException {
        String oldHashedToken = jwtUtil.hashRefreshToken(token.getRefresh());
        Optional<RefreshToken> oldToken = refreshTokenRepository.findByToken(oldHashedToken);

        if (oldToken.isPresent()) {
            RefreshToken currentToken = oldToken.get();
            String username = currentToken.getUsername();

            if (currentToken.getExpiry().isBefore(Instant.now())) {
                //expired, can't refresh
                refreshTokenRepository.delete(currentToken);

                //save deletion of old refresh token
                LOGGER.debug("user={}: deleting expired refresh token",  username);
                accountActivityLogService.save(username, "refresh token request - expired, deleting refresh token");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            } else {
                //active, so refresh (happy path)
                UserDetails userDetails = judgementUserDetailsService.loadUserByUsername(username);

                String accessToken = jwtUtil.generateAccessToken(username, userDetails.getAuthorities());
                String newToken = jwtUtil.generateRefreshToken();
                String newHashedToken = jwtUtil.hashRefreshToken(newToken);
                Instant refreshExpiry = jwtUtil.getRefreshTokenExpiration();

                currentToken.setToken(newHashedToken);
                currentToken.setModifiedBy(username);
                currentToken.setModifiedOn(Instant.now());
                currentToken.setExpiry(refreshExpiry);
                refreshTokenRepository.save(currentToken);

                //save request for refresh token
                LOGGER.debug("user={}: issuing new tokens", username);
                accountActivityLogService.save(username, "refresh token request - granted");

                return ResponseEntity.ok(new JwtTokenResponse(accessToken, newToken));
            }
        }

        return ResponseEntity.badRequest().body("refresh token " + token.getRefresh() + " does not exist");
    }

    //curl -v http://localhost:8080/api/jwtlogout -H "cache-control: no-cache" -H "content-type: application/json" -d "{\"refresh\":\"<refreshtoken>\"}"
    @PostMapping(path = "/api/jwtlogout")
    public ResponseEntity<?> jwtLogout(@RequestBody JwtRefreshTokenRequest token) throws NoSuchAlgorithmException {
        String hashedToken = jwtUtil.hashRefreshToken(token.getRefresh());
        Optional<RefreshToken> tmpOrigToken = refreshTokenRepository.findByToken(hashedToken);

        if (tmpOrigToken.isPresent()) {
            RefreshToken dbToken = tmpOrigToken.get();

            //UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //String username = userDetails.getUsername();
            String username = dbToken.getUsername();

            //delete refresh token
            refreshTokenRepository.delete(dbToken);

            //save logout
            LOGGER.debug("user={}: logout", username);
            accountActivityLogService.save(username, "logout");

            //clear context
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok().body("User " + username + " has been logged out");
        }

        return ResponseEntity.badRequest().body("refresh token " + token.getRefresh() + " does not exist");
    }

    @PostMapping(path = "/api/jwtchange")
    public ResponseEntity<?> changePassword(@RequestBody JwtChangePasswordRequest token) throws NoSuchAlgorithmException {
        if (token.getOldPassword().equals(token.getNewPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("old password and new password are the same");
        }

        //TODO: finish this (change password)

        return ResponseEntity.ok().body("password changed");
    }

    @PostMapping(path = "/api/jwtreset")
    public ResponseEntity<?> resetAccount() throws NoSuchAlgorithmException {

        //TODO: finish this (reset password)

        return ResponseEntity.ok().body("success");
    }


    @PostMapping(path = "/api/jwtcreate")
    public ResponseEntity<?> createAccount() throws NoSuchAlgorithmException {

        //TODO: finish this (create account)

        return ResponseEntity.ok().body("success");
    }

    //curl -v http://localhost:8080/testsecured -H "cache-control: no-cache" -H "content-type: application/json" -H "Authorization: Bearer <accesstoken>"
    @Profile({"local", "dev"})
    @GetMapping("/api/testsecured")
    public ResponseEntity<String> testSecured() {
        return new ResponseEntity<>("<h1 style=\"color=#F00\">Secure end point</h1>", HttpStatus.OK);
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
    public static class JwtRefreshTokenRequest {
        public String refresh;

        protected JwtRefreshTokenRequest() {}

        public JwtRefreshTokenRequest(String refresh) {
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
    public static class JwtLoginRequest {
        private String username;
        private String password;

        protected JwtLoginRequest() {}

        public JwtLoginRequest(final String username, final String password) {
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

    public static class JwtChangePasswordRequest {
        private String oldPassword;
        private String newPassword;

        protected JwtChangePasswordRequest() {}

        public JwtChangePasswordRequest(final String oldPassword, final String newPassword) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }
    }

}
