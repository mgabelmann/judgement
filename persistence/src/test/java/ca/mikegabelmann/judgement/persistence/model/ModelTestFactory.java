package ca.mikegabelmann.judgement.persistence.model;


import java.time.Instant;

public class ModelTestFactory {

    private ModelTestFactory() {}


    public static RoleCode createRoleCode(final boolean active, final String name) {
        return new RoleCode(active, name, name);
    }

    public static AccountStatusCode createAccountStatusCode(final boolean active, final String name) {
        return new AccountStatusCode(active, name, name);
    }

    public static Account createAccount(boolean active, String email, String username, AccountStatusCode status, RoleCode role) {
        Instant now = Instant.now();
        return new Account(null, username, now, username, now, 0L, email, "abcdef", "12345678", username, status, role);
    }

    public static AccountActivityLog createAccountActivityLog(String username, String message) {
        Instant now = Instant.now();
        return new AccountActivityLog(null, now, message, username);
    }
}
