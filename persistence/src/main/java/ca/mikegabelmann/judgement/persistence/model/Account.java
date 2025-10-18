package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ACCOUNT")
public class Account extends AbstractAuditable {
    @Id
    @UuidGenerator
    @Column(name = "ACCOUNT_ID", nullable = false, unique = true)
    private UUID id;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 320)
    private String email;

    @Column(name = "USERNAME", nullable = false, unique = true, length = 16)
    private String username;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "ACTIVE", nullable = false, length = 1)
    private Boolean active;

    @Lob
    @Column(name = "SALT", nullable = false, length = 32, columnDefinition = "CLOB(32)")
    private byte[] salt;

    @Lob
    @Column(name = "PASSWORD", nullable = false, length = 196, columnDefinition = "CLOB(196)")
    private byte[] password;


    protected Account() {
        super();
    }

    public Account(UUID createdBy, UUID modifiedBy, Instant createdOn, Instant modifiedOn, Long version, Boolean active, String email, byte[] password, byte[] salt, String username) {
        super(createdBy, modifiedBy, createdOn, modifiedOn, version);
        this.active = active;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.username = username;
    }

    // OBJECT overrides
    @Override
    public String toString() {
        return "Account{"
                + "id='"
                + id
                + '\''
                + ", active='"
                + active
                + '\''
                + ", email='"
                + email
                + '\''
                + ", password='"
                + password
                + '\''
                + ", salt='"
                + salt
                + '\''
                + ", username='"
                + username
                + '\''
                + ", createdBy='"
                + createdBy
                + '\''
                + ", createdonDtm='"
                + createdOn
                + '\''
                + ", modifiedBy='"
                + modifiedBy
                + '\''
                + ", modifiedonDtm='"
                + modifiedOn
                + '\''
                + ", version='"
                + version
                + '\''
                + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account that = (Account) o;
        return Objects.equals(id, that.id)
                && Objects.equals(active, that.active)
                && Objects.equals(createdBy, that.createdBy)
                && Objects.equals(createdOn, that.createdOn)
                && Objects.equals(email, that.email)
                && Objects.equals(modifiedBy, that.modifiedBy)
                && Objects.equals(modifiedOn, that.modifiedOn)
                && Objects.equals(password, that.password)
                && Objects.equals(salt, that.salt)
                && Objects.equals(username, that.username)
                && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(active);
        result = 31 * result + Objects.hashCode(createdBy);
        result = 31 * result + Objects.hashCode(createdOn);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(modifiedBy);
        result = 31 * result + Objects.hashCode(modifiedOn);
        result = 31 * result + Objects.hashCode(password);
        result = 31 * result + Objects.hashCode(salt);
        result = 31 * result + Objects.hashCode(username);
        result = 31 * result + Objects.hashCode(version);
        return result;
    }

}
