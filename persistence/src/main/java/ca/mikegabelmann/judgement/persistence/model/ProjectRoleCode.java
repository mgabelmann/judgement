package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "PROJECT_ROLE_CODE")
public class ProjectRoleCode extends AbstractCode implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    protected ProjectRoleCode() {
        ;
    }

    public ProjectRoleCode(Boolean active, String label, String code) {
        super(active, label, code);
    }

    @Override
    public String toString() {
        return "ProjectRoleCode" + super.toString();
    }

}
