package ca.mikegabelmann.judgement.codes;

public enum Role {

    /** A role used by the system to do most actions (automated). */
    SYSTEM("System"),

    /** A role used to manage the application. Can do anything. */
    ADMINISTRATOR("Administrator"),

    /** A role used to manage all projects. */
    PROJECT_ADMIN("Project Administrator"),

    /** A project user role with elevated privileges. */
    PROJECT_JUDGE("Project Judge"),

    /** A project user role with regular privileges. */
    PROJECT_CLIENT("Project Client"),

    /** A user with basic credentials (read-only). */
    VIEWER("Viewer"),
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
