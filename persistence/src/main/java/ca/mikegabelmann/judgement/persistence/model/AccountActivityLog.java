package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "ACCOUNT_ACTIVITY_LOG")
public class AccountActivityLog {
    @Id
    @UuidGenerator
    @Column(name = "ID", nullable = false)
    private UUID id;

    @Column(name = "ACTIVITYON_DTM", nullable = false)
    private Instant activityOn;

    @Column(name = "MESSAGE", nullable = false, length = 1024)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", nullable = true)
    private Account account;


    protected AccountActivityLog() {}

    public AccountActivityLog(UUID id, Instant activityOn, String message, Account account) {
        this.id = id;
        this.activityOn = activityOn;
        this.message = message;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "AccountActivityLog{" +
                ", id=" + id +
                ", activityOn=" + activityOn +
                ", account=" + account +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof AccountActivityLog that)) return false;

        return id.equals(that.id)
                && activityOn.equals(that.activityOn)
                && message.equals(that.message)
                && Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + activityOn.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + Objects.hashCode(account);
        return result;
    }

}
