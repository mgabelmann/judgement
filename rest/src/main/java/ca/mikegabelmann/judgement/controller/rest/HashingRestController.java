package ca.mikegabelmann.judgement.controller.rest;

import ca.mikegabelmann.judgement.controller.config.JudgementConfiguration;
import ca.mikegabelmann.judgement.security.WebSecurityConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;

@Profile("local")
@RestController
public class HashingRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HashingRestController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JudgementConfiguration judgementConfiguration;

    //http://localhost:8080/codes/hashpassword?password=123456
    @GetMapping(path = "/codes/hashpassword")
    public ResponseEntity<HashResponse> hashPassword(@RequestParam(name = "password") String password) {
        String secret = judgementConfiguration.getPepper();
        int saltLength = WebSecurityConfiguration.DEFAULT_SALT_LENGTH;
        int iterations = WebSecurityConfiguration.DEFAULT_ITERATIONS;
        String algorithmName = WebSecurityConfiguration.ALGORITHM.name();

        /* Generate a random salt that we store in the DB (1/password). The password is stored as a hash,
         * adding a random salt helps to reduce hash collisions
         */
        String salt = WebSecurityConfiguration.getSalt(WebSecurityConfiguration.DEFAULT_PASSWORD_PREFIX_LENGTH);
        String hashedPassword = passwordEncoder.encode(salt + password);
        //String hashedPassword = passwordEncoder.encode(password);

        LOGGER.info("password={}, salt={}, hash={}", password, salt, hashedPassword);

        //return ResponseEntity.ok(new HashResponse(algorithmName, WebSecurityConfiguration.urlEncode(hashedPassword), iterations, WebSecurityConfiguration.urlEncode(salt), saltLength, secret));
        return ResponseEntity.ok(new HashResponse(algorithmName, hashedPassword, iterations, salt, saltLength, secret));
    }

    @GetMapping(path = "codes/verifypassword")
    public ResponseEntity<Boolean> verifypassword(
            @RequestParam(name = "password") String password,
            @RequestParam(name = "salt") String salt,
            @RequestParam(name = "hash") String hash) {

        boolean matches = passwordEncoder.matches(salt + password, hash);
        //boolean matches = passwordEncoder.matches(password, hash);

        LOGGER.info("password={}, salt={}, hash={}, matches={}", password, salt, hash, matches);

        return ResponseEntity.ok(matches);
    }

    public static class HashResponse {
        public String secret;
        public int saltLength;
        public int iterations;
        public String algorithm;

        public String salt;
        public String hashedPassword;

        public HashResponse(String algorithm, String hashedPassword, int iterations, String salt, int saltLength, String secret) {
            this.algorithm = algorithm;
            this.hashedPassword = hashedPassword;
            this.iterations = iterations;
            this.salt = salt;
            this.saltLength = saltLength;
            this.secret = secret;
        }
    }

}
