package ca.mikegabelmann.judgement.codes;


public enum ProjectRole {
    PROJECT_OWNER("Project Owner"),
    PROJECT_JUDGE("Project Judge"),
    PROJECT_USER("Project User"),
    PROJECT_VIEWER("Project Viewer"),
    ;

    private final String label;

    /**
     * Constructor.
     * @param label label
     */
    ProjectRole(final String label) {
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
     * Get ProjectRole from given string.
     * @param role role
     * @return value
     */
    public static ProjectRole getRole(final String role) {
        return ProjectRole.valueOf(role.toUpperCase());
    }
}
