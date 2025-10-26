package ca.mikegabelmann.judgement.controller.rest;

import ca.mikegabelmann.judgement.security.service.JudgementAuthenticationProviderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRestController.class);

    private final JudgementAuthenticationProviderServiceImpl judgementAuthenticationProviderServiceImpl;


    @Autowired
    public LoginRestController(JudgementAuthenticationProviderServiceImpl judgementAuthenticationProviderServiceImpl) {
        this.judgementAuthenticationProviderServiceImpl = judgementAuthenticationProviderServiceImpl;
    }

    //curl -v http://localhost:8080/login -H "cache-control: no-cache" -H "content-type: application/json" -d "{\"username\":\"ADMIN\",\"password\":\"123456\"}"
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authResponse = judgementAuthenticationProviderServiceImpl.authenticate(authRequest);

        if (authResponse.isAuthenticated()) {
            LOGGER.info("User={}, authentication={}", loginRequest.getUsername(), authResponse.getCredentials());
        }

        //save authentication
        SecurityContextHolder.getContext().setAuthentication(authResponse);

        return new ResponseEntity<>("user authenticated", HttpStatus.OK);
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
