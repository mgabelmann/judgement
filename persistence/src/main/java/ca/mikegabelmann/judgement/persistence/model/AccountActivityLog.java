package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "ACCOUNT_ACTIVITY_LOG")
public class AccountActivityLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "ID", nullable = false)
    private UUID id;

    @Column(name = "ACTIVITYON_DTM", nullable = false)
    private Instant activityOn;

    @Column(name = "MESSAGE", nullable = false, length = 1024)
    private String message;

    @Column(name = "USERNAME", nullable = false, length = 16)
    private String username;


    protected AccountActivityLog() {}

    public AccountActivityLog(final UUID id, final Instant activityOn, final String message, final String username) {
        this.id = id;
        this.activityOn = activityOn;
        this.message = message;
        this.username = username;
    }

    public Instant getActivityOn() {
        return activityOn;
    }

    public void setActivityOn(Instant activityOn) {
        this.activityOn = activityOn;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "AccountActivityLog{" +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", activityOn=" + activityOn +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof AccountActivityLog that)) return false;

        return Objects.equals(id, that.id) &&
                Objects.equals(activityOn, that.activityOn) &&
                Objects.equals(message, that.message)
                && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(activityOn);
        result = 31 * result + Objects.hashCode(message);
        result = 31 * result + Objects.hashCode(username);
        return result;
    }

}
