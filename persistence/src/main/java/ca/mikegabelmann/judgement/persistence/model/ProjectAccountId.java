package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class ProjectAccountId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "PROJECT_ID")
    private UUID projectId;

    @Column(name = "ACCOUNT_ID")
    private UUID accountId;


    protected ProjectAccountId() {
        super();
    }

    public ProjectAccountId(final UUID projectId, final UUID accountId) {
        this.projectId = projectId;
        this.accountId = accountId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public UUID getProjectId() {
        return projectId;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ProjectAccountId that)) return false;

        return projectId.equals(that.projectId) && accountId.equals(that.accountId);
    }

    @Override
    public int hashCode() {
        int result = projectId.hashCode();
        result = 31 * result + accountId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{accountId=" + accountId + ", projectId=" + projectId + "}";
    }

}
