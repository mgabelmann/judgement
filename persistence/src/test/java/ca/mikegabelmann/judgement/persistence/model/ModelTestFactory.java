package ca.mikegabelmann.judgement.persistence.model;


public class ModelTestFactory {

    private ModelTestFactory() {}


    public static RoleCode getRoleCode(final boolean active, final String name) {
        return new RoleCode(active, name, name);
    }

}
