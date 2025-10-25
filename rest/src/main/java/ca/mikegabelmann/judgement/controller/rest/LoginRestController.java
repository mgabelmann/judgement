package ca.mikegabelmann.judgement.controller.rest;

import ca.mikegabelmann.judgement.security.service.AuthenticationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRestController.class);

    private final AuthenticationManager authenticationManager;
    private final AuthenticationServiceImpl authenticationService;

    public LoginRestController(final AuthenticationManager authenticationManager, final AuthenticationServiceImpl authenticationService) {
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
    }

    @GetMapping(path = "/helloworld")
    public ResponseEntity<String> helloworld(@RequestParam(name = "username") String username) {
        LOGGER.info("hello {}", username);
        return ResponseEntity.ok("Hello " + username);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();

        //throws a UsernameNotFoundException if user not found
        String salt = authenticationService.getSalt(loginRequest.getUsername());
        String password = salt + loginRequest.getPassword();

        LOGGER.info("login request: username={}, salt={}, password={}", username, salt, loginRequest.getPassword());

        Authentication authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        //if authentication fails a runtime exception is thrown
        Authentication authResponse = authenticationManager.authenticate(authRequest);

        if (authResponse.isAuthenticated()) {
            LOGGER.info("user={}, authentication=SUCCESS", username);
            return ResponseEntity.ok(authResponse.getName());

        } else {
//            LOGGER.info("user={}, authentication=FAILED", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

//        FIXME: have to save this
//        SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
//        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
    }

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
