package ca.mikegabelmann.judgement.persistence.repository;

import ca.mikegabelmann.judgement.persistence.model.Account;
import ca.mikegabelmann.judgement.persistence.model.AccountActivityLog;
import ca.mikegabelmann.judgement.persistence.model.AccountStatusCode;
import ca.mikegabelmann.judgement.persistence.model.ModelTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(properties = {
//        "spring.datasource.url=jdbc:sqlite:target/memory.db:judgement?cache=shared",
//        "spring.jpa.hibernate.ddl-auto=create-drop",
//})
class AccountActivityLogRepositoryTest {

    private AccountActivityLogRepository accountActivityLogRepository;
    private AccountRepository accountRepository;
    private AccountStatusCodeRepository accountStatusCodeRepository;

    private AccountStatusCode asc1;
    private Account a1;
    private AccountActivityLog acl1;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    public AccountActivityLogRepositoryTest(AccountActivityLogRepository accountActivityLogRepository, AccountRepository accountRepository, AccountStatusCodeRepository accountStatusCodeRepository) {
        this.accountActivityLogRepository = accountActivityLogRepository;
        this.accountRepository = accountRepository;
        this.accountStatusCodeRepository = accountStatusCodeRepository;
    }

    @BeforeEach
    void beforeEach() {
        AccountStatusCode accountStatusCode = ModelTestFactory.createAccountStatusCode(true, "NEW");
        this.asc1 = this.accountStatusCodeRepository.save(accountStatusCode);

        Account account = ModelTestFactory.createAccount(true, "test@dot.com", "test", asc1);
        this.a1 = this.accountRepository.save(account);

        AccountActivityLog accountActivityLog = ModelTestFactory.createAccountActivityLog("this is a test message", a1);
        this.acl1 = this.accountActivityLogRepository.save(accountActivityLog);
    }

    @Test
    @DisplayName("find by account id and ordered - single record")
    void findByAccountIdOrderByActivityOnDesc() {
        List<AccountActivityLog> records = accountActivityLogRepository.findByAccountIdOrderByActivityOnDesc(a1.getId(), Pageable.unpaged());
        Assertions.assertFalse(records.isEmpty());
        Assertions.assertEquals(1, records.size());
    }
}