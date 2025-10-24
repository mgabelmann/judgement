package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.AccountActivityLog;
import ca.mikegabelmann.judgement.persistence.repository.AccountActivityLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class AccountActivityLogServiceImpl implements AccountActivityLogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountActivityLogServiceImpl.class);

    private final AccountActivityLogRepository accountActivityLogRepository;


    @Autowired
    public AccountActivityLogServiceImpl(final AccountActivityLogRepository accountActivityLogRepository) {
        this.accountActivityLogRepository = accountActivityLogRepository;
    }

    @Override
    public List<AccountActivityLog> getLogsByAccountId(final UUID accountId, Pageable pageable) {
        return accountActivityLogRepository.findByAccountIdOrderByActivityOnDesc(accountId, pageable);
    }

}
