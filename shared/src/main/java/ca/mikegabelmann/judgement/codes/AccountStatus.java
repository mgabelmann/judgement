package ca.mikegabelmann.judgement.codes;

public enum AccountStatus {
    /** New account, no email sent yet. */
    NEW("New"),

    /** New account, email sent, but no response yet. */
    PENDING("Pending"),

    /** New account, email response received. */
    ACTIVE("Active"),

    /** Inactive account. As determined by user, admin or system. */
    INACTIVE("Inactive"),

    /** Account suspended. This is a temporary block. */
    SUSPENDED("Suspended"),

    /** Account disabled. This is a permanent closure until admin determines otherwise. */
    BLOCKED("Blocked"),

    /** Account requires a new password. Similar to 'NEW'. */
    PASSWORD_RESET("Password Reset"),
    ;

    private final String label;

    /**
     * Constructor.
     * @param label label
     */
    AccountStatus(final String label) {
        this.label = label;
    }

    public String getDescription() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
