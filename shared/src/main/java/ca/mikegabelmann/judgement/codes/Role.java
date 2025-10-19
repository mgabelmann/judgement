package ca.mikegabelmann.judgement.codes;

public enum Role {

    SYSTEM("System Event"),
    ADMINISTRATOR("Administrator"),

    PROJECT_OWNER("Project Owner"),
    //PROJECT_MANAGER("Project Manager"),

    PROJECT_JUDGE("Project Judge"),
    PROJECT_CLIENT("Project Client"),

    CLIENT("Client"),
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

}
