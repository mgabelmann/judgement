package ca.mikegabelmann.judgement.security.service;

import ca.mikegabelmann.judgement.codes.AccountStatus;
import ca.mikegabelmann.judgement.persistence.model.Account;
import ca.mikegabelmann.judgement.persistence.model.ProjectAccount;
import ca.mikegabelmann.judgement.persistence.repository.AccountRepository;
import ca.mikegabelmann.judgement.persistence.repository.ProjectAccountRepository;
import ca.mikegabelmann.judgement.security.JudgementGrantedAuthority;
import ca.mikegabelmann.judgement.security.JudgementUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
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
    private final ProjectAccountRepository projectAccountRepository;


    @Autowired
    public JudgementUserDetailsServiceImpl(AccountRepository accountRepository, ProjectAccountRepository projectAccountRepository) {
        this.accountRepository = accountRepository;
        this.projectAccountRepository = projectAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByUsername(username);

        if (account.isPresent()) {
            LOGGER.trace("found account for user {}", username);

            Account tmp = account.get();
            AccountStatus accountStatus = AccountStatus.getAccountStatus(tmp.getAccountStatus().getCode());

            //Check account status
            if (accountStatus.equals(AccountStatus.BLOCKED)) {
                throw new DisabledException("Account is blocked");

            } else if (accountStatus.equals(AccountStatus.SUSPENDED)) {
                throw new DisabledException("Account is suspended");

            } else if (accountStatus.equals(AccountStatus.INACTIVE)) {
                throw new DisabledException("Account is inactive");
            }

            //TODO: handle other statuses that require it

            List<JudgementGrantedAuthority> grantedAuthorities = new ArrayList<>();

            {
                JudgementGrantedAuthority role = new JudgementGrantedAuthority("ROLE_" + tmp.getAccountRole().getCode());
                grantedAuthorities.add(role);

                LOGGER.trace("user={}, added role: {}", username, role);
            }

            List<ProjectAccount> projectAccounts = projectAccountRepository.findAllByAccountIs(tmp);
            for (ProjectAccount projectAccount : projectAccounts) {
                JudgementGrantedAuthority authority = new JudgementGrantedAuthority(projectAccount.getProject().getProjectName(), projectAccount.getProjectRole().getCode());
                grantedAuthorities.add(authority);

                LOGGER.trace("user={}, added authority: {}", username, authority);
            }

            return new JudgementUserDetails(tmp.getUsername(), tmp.getPassword(), tmp.getSalt(), AccountStatus.getAccountStatus(tmp.getAccountStatus().getCode()), grantedAuthorities);
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

}
