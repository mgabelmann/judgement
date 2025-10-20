package ca.mikegabelmann.judgement.persistence.model;


import java.time.Instant;
import java.util.UUID;

public class ModelTestFactory {

    private ModelTestFactory() {}


    public static RoleCode createRoleCode(final boolean active, final String name) {
        return new RoleCode(active, name, name);
    }

    public static AccountStatusCode createAccountStatusCode(final boolean active, final String name) {
        return new AccountStatusCode(active, name, name);
    }

    public static Account createAccount(boolean active, String email, String username, AccountStatusCode status) {
        UUID accountId = UUID.randomUUID();
        Instant now = Instant.now();
        return new Account(null, accountId, now, accountId, now, 0L, active, email, "123456".getBytes(), "abcdef".getBytes(), username, status);
    }

    public static AccountActivityLog createAccountActivityLog(String message, Account account) {
        Instant now = Instant.now();
        return new AccountActivityLog(null, now, message, account);
    }
}
