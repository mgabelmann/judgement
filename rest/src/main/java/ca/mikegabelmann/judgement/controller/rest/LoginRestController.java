package ca.mikegabelmann.judgement.controller.rest;

import ca.mikegabelmann.judgement.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRestController.class);

    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final AuthenticationProvider authenticationProvider;
    private final JwtUtil jwtUtil;

    @Autowired
    public LoginRestController(
            final AuthenticationProvider authenticationProvider,
            final SecurityContextRepository securityContextRepository, JwtUtil jwtUtil) {

        this.securityContextRepository = securityContextRepository;
        this.authenticationProvider = authenticationProvider;
        this.jwtUtil = jwtUtil;
    }

    //curl -v http://localhost:8080/login -H "cache-control: no-cache" -H "content-type: application/json" -d "{\"username\":\"ADMIN\",\"password\":\"123456\"}"
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

    @PostMapping(path = "/jwtlogin")
    public ResponseEntity<JwtTokenResponse> jwtLogin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, loginRequest.getPassword());

        Authentication authResponse = authenticationProvider.authenticate(authRequest);

        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        return ResponseEntity.ok(new JwtTokenResponse(accessToken, refreshToken));
    }

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
    public class JwtTokenResponse {
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
     * Required for login requests.
     */
    public static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest() {}

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
