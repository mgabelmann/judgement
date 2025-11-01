package ca.mikegabelmann.judgement.controller.rest;

import ca.mikegabelmann.judgement.config.WebSecurityConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("local")
@RestController
public class HashingRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HashingRestController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebSecurityConfiguration webSecurityConfiguration;

    //http://localhost:8080/codes/hashpassword?password=123456
    @GetMapping(path = "/codes/hashpassword")
    public ResponseEntity<HashResponse> hashPassword(@RequestParam(name = "password") String password) {
        String algorithmName = WebSecurityConfiguration.DEFAULT_ENCODING_ID;

        /* Generate a random salt that we store in the DB (1/password). The password is stored as a hash,
         * adding a random salt helps to reduce hash collisions
         */
        //String salt = WebSecurityConfiguration.getRandomSalt(WebSecurityConfiguration.DEFAULT_PASSWORD_PREFIX_LENGTH);
        String salt = "";
        String hashedPassword = passwordEncoder.encode(salt + password);

        LOGGER.info("password={}, salt={}, hash={}", password, salt, hashedPassword);

        return ResponseEntity.ok(new HashResponse(algorithmName, hashedPassword, salt));
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
        public String algorithm;
        public String salt;
        public String hashedPassword;

        public HashResponse(String algorithm, String hashedPassword, String salt) {
            this.algorithm = algorithm;
            this.hashedPassword = hashedPassword;
            this.salt = salt;
        }
    }

}
