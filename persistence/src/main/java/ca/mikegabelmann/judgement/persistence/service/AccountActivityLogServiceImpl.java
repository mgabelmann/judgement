package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.AccountActivityLog;
import ca.mikegabelmann.judgement.persistence.repository.AccountActivityLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service
public class AccountActivityLogServiceImpl implements AccountActivityLogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountActivityLogServiceImpl.class);

    private final AccountActivityLogRepository accountActivityLogRepository;


    @Autowired
    public AccountActivityLogServiceImpl(final AccountActivityLogRepository accountActivityLogRepository) {
        this.accountActivityLogRepository = accountActivityLogRepository;
    }

    @Override
    public List<AccountActivityLog> getLogsByUsername(final String username, Pageable pageable) {
        return accountActivityLogRepository.findByUsernameOrderByActivityOnDesc(username, pageable);
    }

    @Override
    public void save(final String username, final String message) {
        AccountActivityLog log = new AccountActivityLog(null, Instant.now(), message, username);
        accountActivityLogRepository.save(log);
    }

}
