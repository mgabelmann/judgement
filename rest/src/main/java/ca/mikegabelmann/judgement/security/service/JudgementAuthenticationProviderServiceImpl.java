package ca.mikegabelmann.judgement.security.service;

import ca.mikegabelmann.judgement.security.JudgementUserDetails;
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
public class JudgementAuthenticationProviderServiceImpl implements AuthenticationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JudgementAuthenticationProviderServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final JudgementUserDetailsServiceImpl  judgementUserDetailsServiceImpl;

    @Autowired
    public JudgementAuthenticationProviderServiceImpl(final JudgementUserDetailsServiceImpl judgementUserDetailsServiceImpl, final PasswordEncoder passwordEncoder) {
        this.judgementUserDetailsServiceImpl = judgementUserDetailsServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();

        try {
            //may throw a UsernameNotFoundException if account not found
            JudgementUserDetails judgementUserDetails = (JudgementUserDetails) judgementUserDetailsServiceImpl.loadUserByUsername(username);

            LOGGER.info("userdetails: {}", judgementUserDetails);

            String password = judgementUserDetails.getSalt() + authentication.getCredentials().toString();
            String hashedPassword = judgementUserDetails.getPassword();

            if (passwordEncoder.matches(password, hashedPassword)) {
                //TODO: log success to AccountActivityLog
                return UsernamePasswordAuthenticationToken.authenticated(username, hashedPassword, judgementUserDetails.getAuthorities());

            } else {
                throw new BadCredentialsException("Invalid credentials for " + username);
            }

        } catch (final AuthenticationException e) {
            //TODO: log failure to AccountActivityLog
            LOGGER.info(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
