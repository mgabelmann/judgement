package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;


@Entity
@Table(name = "ROLE_CODE")
public class RoleCode extends AbstractCode implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    protected RoleCode() {
        ;
    }

    public RoleCode(Boolean active, String label, String code) {
        super(active, label, code);
    }

    @Override
    public String toString() {
        return "RoleCode" + super.toString();
    }

}
