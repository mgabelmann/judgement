package ca.mikegabelmann.judgement.security;

import ca.mikegabelmann.judgement.controller.config.JudgementConfiguration;
import ca.mikegabelmann.judgement.security.service.JudgementAuthenticationProvider;
import ca.mikegabelmann.judgement.security.service.JudgementUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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

    public static final int DEFAULT_PASSWORD_PREFIX_LENGTH = 8;

    private final JudgementConfiguration judgementConfiguration;
    //private final JudgementUserDetailsService judgementUserDetailsService;
    //private final JudgementAuthenticationProvider judgementAuthenticationProvider;


//    @Autowired
//    public WebSecurityConfiguration(JudgementConfiguration judgementConfiguration, JudgementAuthenticationProvider judgementAuthenticationProvider) {
//        this.judgementConfiguration = judgementConfiguration;
//        this.judgementAuthenticationProvider = judgementAuthenticationProvider;
//    }

//    @Autowired
//    public WebSecurityConfiguration(JudgementConfiguration judgementConfiguration, JudgementUserDetailsService judgementUserDetailsService) {
//        this.judgementConfiguration = judgementConfiguration;
//        this.judgementUserDetailsService = judgementUserDetailsService;
//    }

    @Autowired
    public WebSecurityConfiguration(JudgementConfiguration judgementConfiguration) {
        this.judgementConfiguration = judgementConfiguration;
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/actuator/**").hasRole("ADMINISTRATOR")
                .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico").permitAll()
                .requestMatchers("/codes/**").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/loginsuccess").permitAll()
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())

            //used to store JSESSIONID
            .securityContext((securityContext) -> securityContext.securityContextRepository(securityContextRepository()))
            .requestCache(RequestCacheConfigurer::disable)

            //.formLogin(Customizer.withDefaults())
            .formLogin(AbstractHttpConfigurer::disable)
            //.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .logout((logout) -> logout.clearAuthentication(true).invalidateHttpSession(true).logoutUrl("/logout").permitAll().deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler()))
        ;

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
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return this.createDelegatingPasswordEncoder();
    }

    public static final String DEFAULT_ENCODING_ID = "argon2@SpringSecurity_v5_8";

    public PasswordEncoder createDelegatingPasswordEncoder() {
        String secret = judgementConfiguration.getPepper();
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
//        encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
//        encoders.put("sha256", new StandardPasswordEncoder());
//        encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_2());
        encoders.put("argon2@SpringSecurity_v5_8", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());

        //custom encoders
        encoders.put("judgeargon2", new Argon2PasswordEncoder(16, 32, 1, 24576, 2));
        encoders.put("judgepbkdf2", new Pbkdf2PasswordEncoder(secret, 16, 310000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256));

          return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    /**
     * Get a random 'salt' for encryption purposes.
     * @return
     */
    public static String getRandomSalt(int saltLength) {
        byte[] salt = new byte[saltLength];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * URL encode a string.
     * @param value
     * @return
     */
    public static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
