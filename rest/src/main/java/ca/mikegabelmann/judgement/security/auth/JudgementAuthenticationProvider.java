package ca.mikegabelmann.judgement.security.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
//public class JudgementAuthenticationProvider implements AuthenticationProvider {
public class JudgementAuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JudgementAuthenticationProvider.class);

    private final PasswordEncoder passwordEncoder;
    private final JudgementUserDetailsService judgementUserDetailsService;

    @Autowired
    public JudgementAuthenticationProvider(final JudgementUserDetailsService judgementUserDetailsService, final PasswordEncoder passwordEncoder) {
        this.judgementUserDetailsService = judgementUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    //@Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();

        try {
            //may throw a UsernameNotFoundException if account not found
            JudgementUserDetails judgementUserDetails = (JudgementUserDetails) judgementUserDetailsService.loadUserByUsername(username);

            LOGGER.info("userdetails: {}", judgementUserDetails);

            String password = judgementUserDetails.getSalt() + authentication.getCredentials().toString();
            String hashedPassword = judgementUserDetails.getPassword();

            if (passwordEncoder.matches(password, hashedPassword)) {
                //TODO: log success to AccountActivityLog
                return UsernamePasswordAuthenticationToken.authenticated(judgementUserDetails, hashedPassword, judgementUserDetails.getAuthorities());

            } else {
                throw new BadCredentialsException("Invalid credentials for " + username);
            }

        } catch (final AuthenticationException e) {
            //TODO: log failure to AccountActivityLog
            LOGGER.info(e.getMessage());
            throw e;
        }
    }

    //@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
