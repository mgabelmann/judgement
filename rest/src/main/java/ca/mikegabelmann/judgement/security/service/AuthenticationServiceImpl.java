package ca.mikegabelmann.judgement.security.service;

import ca.mikegabelmann.judgement.persistence.model.Account;
import ca.mikegabelmann.judgement.persistence.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final AccountRepository accountRepository;

    @Autowired
    public AuthenticationServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     *
     * @param username
     * @return
     * @throws AuthenticationException
     */
    public String getSalt(final String username) throws AuthenticationException {
        Optional<Account> account = accountRepository.findByUsername(username);

        if (account.isPresent()) {
            return account.get().getSalt();

        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }

}
