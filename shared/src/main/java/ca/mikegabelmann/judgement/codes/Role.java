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

    private final String descriptiion;

    /**
     * Constructor.
     * @param description description
     */
    Role(final String description) {
        this.descriptiion = description;
    }

    public String getDescription() {
        return descriptiion;
    }

    @Override
    public String toString() {
        return descriptiion;
    }

}
