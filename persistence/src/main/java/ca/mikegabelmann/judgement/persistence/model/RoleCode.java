package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "ROLE_CODE")
public class RoleCode implements Serializable {
    @Id
    @Column(name = "ID", nullable = false, length = 32, unique = true)
    private String id;

    @Column(name = "DESCRIPTION", nullable = false, length = 254)
    private String description;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "ACTIVE", nullable = false, length = 1)
    private Boolean active;


    protected RoleCode() {
        ;
    }

    public RoleCode(Boolean active, String description, String id) {
        this.active = active;
        this.description = description;
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof RoleCode roleCode)) return false;

        return id.equals(roleCode.id) && description.equals(roleCode.description) && active.equals(roleCode.active);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + active.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String sb = "RoleCode{" + "active=" + active +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
        return sb;
    }

}
