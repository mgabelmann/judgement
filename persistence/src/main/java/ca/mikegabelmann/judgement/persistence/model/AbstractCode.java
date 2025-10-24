package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractCode {
    @Id
    @Column(name = "CODE", nullable = false, length = 16, unique = true)
    private String code;

    @Column(name = "LABEL", nullable = false, length = 128)
    private String label;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "ACTIVE", nullable = false, length = 1)
    private Boolean active;


    protected AbstractCode() {
        ;
    }

    public AbstractCode(Boolean active, String label, String code) {
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
    public boolean equals(Object o) {
        if (!(o instanceof AbstractCode code)) return false;

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
        return '{' +
                "code='" + code + '\'' +
                ", active=" + active +
                ", label='" + label + '\'' +
                '}';
    }

}
