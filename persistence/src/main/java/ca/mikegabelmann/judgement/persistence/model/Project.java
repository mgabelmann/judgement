package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "PROJECT")
public class Project implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "PROJECT_ID", nullable = false, unique = true)
    private UUID id;

    @Column(name = "NAME", nullable = false, unique = false, length = 64)
    private String projectName;

    @Column(name = "DESCRIPTION", nullable = false, unique = false, length = 1024)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private Account owner;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "ACTIVE", nullable = false, length = 1)
    private Boolean active;

    @OneToMany(mappedBy = "project")
    private Set<ProjectAccount> projectAccounts;


    protected Project() {

    }

    public Project(Boolean active, String description, UUID id, Account owner, String projectName) {
        this.active = active;
        this.description = description;
        this.id = id;
        this.owner = owner;
        this.projectName = projectName;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Set<ProjectAccount> getProjectAccounts() {
        return projectAccounts;
    }

}
