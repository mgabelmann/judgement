package ca.mikegabelmann.judgement.security.service;

import ca.mikegabelmann.judgement.codes.AccountStatus;
import ca.mikegabelmann.judgement.persistence.model.Account;
import ca.mikegabelmann.judgement.persistence.repository.AccountRepository;
import ca.mikegabelmann.judgement.security.JudgementGrantedAuthority;
import ca.mikegabelmann.judgement.security.JudgementUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class JudgementUserDetailsServiceImpl implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JudgementUserDetailsServiceImpl.class);

    private final AccountRepository accountRepository;


    @Autowired
    public JudgementUserDetailsServiceImpl(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByUsername(username);

        if (account.isPresent()) {
            Account tmp = account.get();
            List<JudgementGrantedAuthority> grantedAuthorities = new ArrayList<>();
            //TODO: load and add granted authorities

            return new JudgementUserDetails(tmp.getUsername(), new String(tmp.getPassword()), AccountStatus.getAccountStatus(tmp.getAccountStatus().getCode()), grantedAuthorities);
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

}
