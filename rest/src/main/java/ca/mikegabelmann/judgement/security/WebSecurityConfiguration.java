package ca.mikegabelmann.judgement.security;

import ca.mikegabelmann.judgement.security.service.JudgementUserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfiguration.class);
    
    private final boolean securityDebug = true;


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
        return web -> web.debug(securityDebug).ignoring().requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

}
