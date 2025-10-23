package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "PROJECT_ACCOUNT")
public class ProjectAccount implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ProjectAccountId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "ACTIVE", nullable = false, length = 1)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_CODE", nullable = false)
    private RoleCode role;

    protected ProjectAccount() {}

    public ProjectAccount(ProjectAccountId id, Boolean active, RoleCode role) {
        this.id = id;
        this.active = active;
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ProjectAccountId getId() {
        return id;
    }

    public void setId(ProjectAccountId id) {
        this.id = id;
    }

    public RoleCode getRole() {
        return role;
    }

    public void setRole(RoleCode role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "ProjectAccount{" + "id=" + id +
                ", active=" + active +
                ", role=" + role +
                '}';
    }

}
