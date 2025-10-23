package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;


@Entity
@Table(name = "ACCOUNT_STATUS_CODE")
public class AccountStatusCode implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CODE", nullable = false, length = 32, unique = true)
    private String code;

    @Column(name = "LABEL", nullable = false, length = 254)
    private String label;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "ACTIVE", nullable = false, length = 1)
    private Boolean active;


    protected AccountStatusCode() {
        ;
    }

    public AccountStatusCode(Boolean active, String label, String code) {
        this.active = active;
        this.label = label;
        this.code = code;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof AccountStatusCode code)) return false;

        return code.equals(code.code) && label.equals(code.label) && active.equals(code.active);
    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + label.hashCode();
        result = 31 * result + active.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String sb = "AccountStatusCode{" + "active=" + active +
                ", code='" + code + '\'' +
                ", label='" + label + '\'' +
                '}';
        return sb;
    }
}
