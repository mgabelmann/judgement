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
    @JoinColumn(name = "PROJECT_ROLE_CODE", nullable = false)
    private ProjectRoleCode projectRole;


    protected ProjectAccount() {}

    public ProjectAccount(ProjectAccountId id, Boolean active, ProjectRoleCode projectRole) {
        this.id = id;
        this.active = active;
        this.projectRole = projectRole;
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

    public ProjectRoleCode getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(ProjectRoleCode projectRole) {
        this.projectRole = projectRole;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "ProjectAccount{" +
                "id=" + id +
                ", active=" + active +
                ", projectRole=" + projectRole +
                '}';
    }

}
