package ca.mikegabelmann.judgement.security.service;

import ca.mikegabelmann.judgement.persistence.model.Account;
import ca.mikegabelmann.judgement.persistence.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Deprecated
@Service
public class JudgementAuthenticationProviderServiceImpl implements AuthenticationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JudgementAuthenticationProviderServiceImpl.class);

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public JudgementAuthenticationProviderServiceImpl(final AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();

        Optional<Account> tmpAccount = accountRepository.findByUsername(username);

        if (tmpAccount.isPresent()) {
            Account account = tmpAccount.get();
            String password = account.getSalt() + authentication.getCredentials().toString();

            if (passwordEncoder.matches(password, account.getPassword())) {
                LOGGER.info("authentication successful for user {}", username);
                return UsernamePasswordAuthenticationToken.authenticated(username, password, List.of());

            } else {
                LOGGER.info("incorrect password for {}", username);
            }

        } else {
            LOGGER.info("account not found for {}", username);
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
