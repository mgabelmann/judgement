package ca.mikegabelmann.judgement.persistence.repository;

import ca.mikegabelmann.judgement.persistence.model.Account;
import ca.mikegabelmann.judgement.persistence.model.AccountActivityLog;
import ca.mikegabelmann.judgement.persistence.model.AccountStatusCode;
import ca.mikegabelmann.judgement.persistence.model.ModelTestFactory;
import ca.mikegabelmann.judgement.persistence.model.RoleCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(properties = {
//        "spring.datasource.url=jdbc:sqlite:target/memory.db:judgement?cache=shared",
//        "spring.jpa.hibernate.ddl-auto=create-drop",
//})
class AccountActivityLogRepositoryTest {

    private final AccountActivityLogRepository accountActivityLogRepository;
    private final AccountRepository accountRepository;
    private final AccountStatusCodeRepository accountStatusCodeRepository;
    private final RoleCodeRepository roleCodeRepository;

    private AccountStatusCode asc1;
    private RoleCode rc1;
    private Account a1;
    private AccountActivityLog aal1;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    public AccountActivityLogRepositoryTest(AccountActivityLogRepository accountActivityLogRepository, AccountRepository accountRepository, AccountStatusCodeRepository accountStatusCodeRepository, RoleCodeRepository roleCodeRepository) {
        this.accountActivityLogRepository = accountActivityLogRepository;
        this.accountRepository = accountRepository;
        this.accountStatusCodeRepository = accountStatusCodeRepository;
        this.roleCodeRepository = roleCodeRepository;
    }

    @BeforeEach
    void beforeEach() {
        AccountStatusCode accountStatusCode = ModelTestFactory.createAccountStatusCode(true, "NEW");
        this.asc1 = this.accountStatusCodeRepository.save(accountStatusCode);

        RoleCode roleCode = ModelTestFactory.createRoleCode(true, "ROLE_USER");
        this.rc1 = roleCodeRepository.save(roleCode);


        Account account = ModelTestFactory.createAccount(true, "test@dot.com", "test", asc1, rc1);
        this.a1 = this.accountRepository.save(account);

        AccountActivityLog accountActivityLog = ModelTestFactory.createAccountActivityLog(a1.getUsername(), "this is a test message 1");
        this.aal1 = this.accountActivityLogRepository.save(accountActivityLog);
    }

    @Test
    @DisplayName("find by account id and ordered - single record")
    void test1_findByAccountIdOrderByActivityOnDesc() {
        List<AccountActivityLog> records = accountActivityLogRepository.findByUsernameOrderByActivityOnDesc(a1.getUsername(), Pageable.unpaged());
        Assertions.assertFalse(records.isEmpty());
        Assertions.assertEquals(1, records.size());
    }

    @Test
    @DisplayName("find by account id and ordered - multiple records")
    void text2_findByAccountIdOrderByActivityOnDesc() {
        AccountActivityLog aal2 = ModelTestFactory.createAccountActivityLog(a1.getUsername(), "this is a test message 2");
        AccountActivityLog aal3 = ModelTestFactory.createAccountActivityLog(a1.getUsername(), "this is a test message 3");

        aal2.setActivityOn(Instant.now().plusSeconds(5));
        aal3.setActivityOn(Instant.now().plusSeconds(1));

        this.accountActivityLogRepository.save(aal2);
        this.accountActivityLogRepository.save(aal3);

        List<AccountActivityLog> records = accountActivityLogRepository.findByUsernameOrderByActivityOnDesc(a1.getUsername(), Pageable.unpaged());

        Assertions.assertFalse(records.isEmpty());
        Assertions.assertEquals(3, records.size());

        Assertions.assertEquals("this is a test message 2", records.get(0).getMessage());
        Assertions.assertEquals("this is a test message 3", records.get(1).getMessage());
        Assertions.assertEquals("this is a test message 1", records.get(2).getMessage());
    }

}