package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.AccountStatusCode;
import ca.mikegabelmann.judgement.persistence.repository.AccountStatusCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountStatusCodeServiceImpl implements AccountStatusCodeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountStatusCodeServiceImpl.class);

    private final AccountStatusCodeRepository accountStatusCodeRepository;

    @Autowired
    public AccountStatusCodeServiceImpl(AccountStatusCodeRepository accountStatusCodeRepository) {
        this.accountStatusCodeRepository = accountStatusCodeRepository;
    }


    @Override
    public List<AccountStatusCode> findAll() {
        return accountStatusCodeRepository.findAll();
    }

    @Override
    public Optional<AccountStatusCode> findById(String id) {
        return accountStatusCodeRepository.findById(id);
    }

    @Override
    public List<AccountStatusCode> findActive() {
        return accountStatusCodeRepository.findAllByActiveTrue();
    }

}
