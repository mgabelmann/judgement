package ca.mikegabelmann.judgement.codes;

public enum Role {

    /** A role used by the system to do most actions (automated). */
    SYSTEM("System"),

    /** A role used to manage the application. Can do anything. */
    ADMINISTRATOR("Administrator"),

    DEVELOPER("Developer"),

    TESTER("Tester"),

    /** A user with basic credentials (read-only). */
    USER("USER"),

    /** REST client. */
    //API_CLIENT("API Client"),
    ;

    private final String label;

    /**
     * Constructor.
     * @param label label
     */
    Role(final String label) {
        this.label = label;
    }

    public String getDescription() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    /**
     * Get Role from given string.
     * @param role role
     * @return value
     */
    public static Role getRole(final String role) {
        return Role.valueOf(role.toUpperCase());
    }

}
