package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;


@Entity
@Table(name = "ACCOUNT_STATUS_CODE")
public class AccountStatusCode extends AbstractCode implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    protected AccountStatusCode() {
        ;
    }

    public AccountStatusCode(Boolean active, String label, String code) {
        super(active, label, code);
    }

    @Override
    public String toString() {
        return "AccountStatusCode" + super.toString();
    }

}
