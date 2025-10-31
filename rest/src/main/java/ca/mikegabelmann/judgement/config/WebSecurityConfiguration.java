package ca.mikegabelmann.judgement.config;

import ca.mikegabelmann.judgement.security.jwt.JwtAuthenticationEntryPoint;
import ca.mikegabelmann.judgement.security.jwt.JwtRequestFilter;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    public static final String DEFAULT_ENCODING_ID = "argon2@SpringSecurity_v5_8";

    /**  */
    @Value("${judgement.security.pepper}")
    private String pepper;

    /**  */
    @Value("${judgement.security.web.debug:false}")
    private boolean securityDebug;

    private final JudgementConfiguration judgementConfiguration;
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Autowired
    public WebSecurityConfiguration(JudgementConfiguration judgementConfiguration, JwtRequestFilter jwtRequestFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.judgementConfiguration = judgementConfiguration;
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @PostConstruct
    public void postConstruct() {
        if (pepper == null || pepper.isEmpty()) {
            //pepper is required for using the application
            throw new NullPointerException("Pepper can not be null or empty. You MUST set this value, preferably as an environment variable.");

        } else if (judgementConfiguration.isProfileActive("local")) {
            LOGGER.warn("security pepper={}. NOTE: only displayed when using 'local' profile", pepper);

        } else {
            LOGGER.info("security pepper has been successfully set");
        }
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/actuator/**").hasRole("ADMINISTRATOR")
                .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico").permitAll()
                .requestMatchers("/codes/**").permitAll()
                //.requestMatchers("/login").permitAll()
                .requestMatchers("/jwtlogin").permitAll()
                .requestMatchers("/jwtrefresh").permitAll()
                .requestMatchers("/loginsuccess").permitAll()
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())

            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))

            //used to store JSESSIONID
            .securityContext((securityContext) -> securityContext.securityContextRepository(securityContextRepository()))
            .requestCache(RequestCacheConfigurer::disable)

            //.formLogin(Customizer.withDefaults())
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .logout((logout) -> logout.clearAuthentication(true).invalidateHttpSession(true).logoutUrl("/logout").permitAll().deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler()))
        ;

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().write("{\"statusCode\":200,\"message\":\"You have been logged out successfully.\",\"data\":true}");
        };
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository());
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        String secret = judgementConfiguration.getPepper();
//        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret, DEFAULT_SALT_LENGTH, DEFAULT_ITERATIONS, ALGORITHM);
//        encoder.setEncodeHashAsBase64(true);
//        return encoder;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        String secret = this.pepper;
        String encodingId = DEFAULT_ENCODING_ID;

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
//        encoders.put("ldap", new LdapShaPasswordEncoder());
//        encoders.put("MD4", new Md4PasswordEncoder());
//        encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
//        encoders.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_5());
//        encoders.put("pbkdf2@SpringSecurity_v5_8", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
//        encoders.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v4_1());
        encoders.put("scrypt@SpringSecurity_v5_8", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
//        encoders.put("SHA-1", new MessageDigestPasswordEncoder("SHA-1"));
        encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
//        encoders.put("sha256", new StandardPasswordEncoder());
//        encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_2());
        encoders.put("argon2@SpringSecurity_v5_8", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());

        //custom encoders
        encoders.put("judgeargon2", new Argon2PasswordEncoder(16, 32, 1, 24576, 2));
        encoders.put("judgepbkdf2", new Pbkdf2PasswordEncoder(secret, 16, 310000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256));

        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    public boolean isSecurityDebug() {
        return securityDebug;
    }

    /**
     * Get a random 'salt' for encryption purposes.
     * @return
     */
    public static byte[] getRandomSalt(final int saltLength) {
        byte[] salt = new byte[saltLength];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static String base64Encode(final byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     *
     * @param s
     * @return
     */
    public static String base64Encode(final String s) {
        return Base64.getEncoder().encodeToString(s.getBytes());
    }

    /**
     * URL encode a string.
     * @param value
     * @return
     */
    public static String urlEncode(final String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public static String bytesToHex(final byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }

}
