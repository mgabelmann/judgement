package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "REFRESH_TOKEN")
public class RefreshToken extends AbstractAuditable {
    @Id
    @UuidGenerator
    @Column(name = "TOKEN_ID", nullable = false, unique = true)
    private UUID id;

    @Column(name = "TOKEN", nullable = false, unique = true)
    private String token;

    @Column(name = "EXPIRY_DTM", nullable = false)
    private Instant expiry;

//    @OneToOne(mappedBy = "token")
//    private Account account;

    @Column(name = "username", nullable = false, unique = true)
    private String username;


    protected RefreshToken() {
        super();
    }

    public RefreshToken(UUID id, String username, Instant expiry, String token, String createdBy, Instant createdOn, String modifiedBy, Instant modifiedOn, Long version) {
        super(createdBy, createdOn, modifiedBy, modifiedOn, version);
        this.id = id;
        this.username = username;
        this.expiry = expiry;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getExpiry() {
        return expiry;
    }

    public void setExpiry(Instant expiry) {
        this.expiry = expiry;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof RefreshToken that)) return false;

        return id.equals(that.id) && token.equals(that.token) && expiry.equals(that.expiry) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + token.hashCode();
        result = 31 * result + expiry.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", expiry='" + expiry + '\'' +
                ", token='" + token + '\'' +
                super.toString() +
                '}';
    }

}
