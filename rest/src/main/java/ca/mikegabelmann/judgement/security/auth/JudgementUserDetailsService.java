package ca.mikegabelmann.judgement.security.auth;

import ca.mikegabelmann.judgement.codes.AccountStatus;
import ca.mikegabelmann.judgement.persistence.model.Account;
import ca.mikegabelmann.judgement.persistence.model.ProjectAccount;
import ca.mikegabelmann.judgement.persistence.repository.AccountRepository;
import ca.mikegabelmann.judgement.persistence.repository.ProjectAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;


@Service
public class JudgementUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JudgementUserDetailsService.class);

    private final AccountRepository accountRepository;
    private final ProjectAccountRepository projectAccountRepository;


    @Autowired
    public JudgementUserDetailsService(final AccountRepository accountRepository, final ProjectAccountRepository projectAccountRepository) {
        this.accountRepository = accountRepository;
        this.projectAccountRepository = projectAccountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Optional<Account> tmpAccount = accountRepository.findByUsername(username);

        if (tmpAccount.isPresent()) {
            LOGGER.trace("found account for user {}", username);

            return this.getUserDetails(tmpAccount.get());
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    @Transactional(readOnly = true)
    public Account loadAccountByUsername(final String username) throws UsernameNotFoundException {
        Optional<Account> tmpAccount = accountRepository.findByUsername(username);

        if (tmpAccount.isPresent()) {
            LOGGER.trace("found account for user {}", username);
            return tmpAccount.get();
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    /**
     *
     * @param account
     * @return
     */
    @Transactional(readOnly = true)
    public UserDetails getUserDetails(final Account account) {
        String username = account.getUsername();

        AccountStatus accountStatus = AccountStatus.getAccountStatus(account.getAccountStatus().getCode());

        //Check account status
        if (accountStatus.equals(AccountStatus.BLOCKED)) {
            throw new DisabledException("Account " + username + " is blocked");

        } else if (accountStatus.equals(AccountStatus.SUSPENDED)) {
            throw new DisabledException("Account " + username + " is suspended");

        } else if (accountStatus.equals(AccountStatus.INACTIVE)) {
            throw new DisabledException("Account " + username + " is inactive");
        }

        //TODO: handle other statuses that require it

        Set<JudgementGrantedAuthority> grantedAuthorities = new TreeSet<>();

        {
            JudgementGrantedAuthority role = JudgementGrantedAuthority.createRole(account.getAccountRole().getCode());
            grantedAuthorities.add(role);

            LOGGER.trace("user={}, added role: {}", username, role);
        }

        List<ProjectAccount> projectAccounts = projectAccountRepository.findAllByAccountIs(account);
        for (ProjectAccount projectAccount : projectAccounts) {
            JudgementGrantedAuthority authority = JudgementGrantedAuthority.createProjectAuthority(projectAccount.getProject().getProjectName(), projectAccount.getProjectRole().getCode());
            grantedAuthorities.add(authority);

            LOGGER.trace("user={}, added authority: {}", username, authority);
        }

        return new JudgementUserDetails(account.getUsername(), account.getPassword(), account.getSalt(), accountStatus, grantedAuthorities);
    }

}
