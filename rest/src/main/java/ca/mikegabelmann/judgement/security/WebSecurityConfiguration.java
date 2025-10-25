package ca.mikegabelmann.judgement.security;

import ca.mikegabelmann.judgement.controller.config.JudgementConfiguration;
import ca.mikegabelmann.judgement.security.service.JudgementUserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    public static final int DEFAULT_SALT_LENGTH = 16;
    public static final int DEFAULT_ITERATIONS = 310000;
    public static final Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm ALGORITHM = Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256;

    public static final int DEFAULT_PASSWORD_PREFIX_LENGTH = 8;


    @Autowired
    private JudgementConfiguration judgementConfiguration;

    @Bean
    public UserDetailsService judgementUserDetailsService(JudgementUserDetailsServiceImpl judgementUserDetailsService) {
        return judgementUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http, final JudgementUserDetailsServiceImpl userDetailsService, final PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
            .csrf(Customizer.withDefaults())
            .formLogin((form) -> form.loginPage("/login").permitAll())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/actuator/**").hasRole("ADMINISTRATOR")
                .requestMatchers("/codes/**").permitAll()
            .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .authenticationManager(authenticationManager)
            .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .logout((logout) -> logout.logoutSuccessUrl("/logout").permitAll())
        ;

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        boolean securityDebug = judgementConfiguration.isSecurityDebug();
        return web -> web.debug(securityDebug).ignoring().requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

        String secret = judgementConfiguration.getPepper();
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret, DEFAULT_SALT_LENGTH, DEFAULT_ITERATIONS, ALGORITHM);
        encoder.setEncodeHashAsBase64(true);
        return encoder;
    }

    /**
     * Get a random 'salt' for encryption purposes.
     * @return
     */
    public static String getSalt(int saltLength) {
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
