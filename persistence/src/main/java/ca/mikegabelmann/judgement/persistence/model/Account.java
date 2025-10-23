package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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

    /**
     * Used for login purposes and verifying an account or password reset.
     */
    @Column(name = "EMAIL", nullable = false, unique = true, length = 320)
    private String email;

    /**
     * Username is a unique id, that is used for display purposes only.
     */
    @Column(name = "USERNAME", nullable = false, unique = true, length = 16)
    private String username;

//    /**
//     * Is the account active or inactive.
//     */
//    @Convert(converter = BooleanConverter.class)
//    @Column(name = "ACTIVE", nullable = false, length = 1)
//    private Boolean active;

//    /**
//     * Used for password hashing. Every account has a random value.
//     */
//    @Lob
//    @Column(name = "SALT", nullable = false, length = 32, columnDefinition = "CLOB(32)")
//    private byte[] salt;

    /**
     * This is a hashed password using a random 'salt' (1/account) and a 'pepper' (1/system).
     */
    @Lob
    @Column(name = "PASSWORD", nullable = false, length = 256, columnDefinition = "CLOB(196)")
    private byte[] password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_STATUS", nullable = false)
    private AccountStatusCode accountStatus;


    /** No arg constructor for use by JPA. */
    protected Account() {
        super();
    }

    public Account(UUID id, String createdBy, Instant createdOn, String modifiedBy, Instant modifiedOn, Long version, Boolean active, String email, byte[] password, byte[] salt, String username, AccountStatusCode accountStatus) {
        super(createdBy, createdOn, modifiedBy, modifiedOn, version);
        this.id = id;
        //this.active = active;
        this.email = email;
        this.password = password;
        //this.salt = salt;
        this.username = username;
        this.accountStatus = accountStatus;
    }

    public AccountStatusCode getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatusCode accountStatus) {
        this.accountStatus = accountStatus;
    }

//    public Boolean getActive() {
//        return active;
//    }
//
//    public void setActive(Boolean active) {
//        this.active = active;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

//    public byte[] getSalt() {
//        return salt;
//    }
//
//    public void setSalt(byte[] salt) {
//        this.salt = salt;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // OBJECT overrides
    @Override
    public String toString() {
        return "Account{"
                + "id='"
                + id
                + '\''
//                + ", active='"
//                + active
//                + '\''
                + ", email='"
                + email
                + '\''
                + ", password='"
                + password
                + '\''
//                + ", salt='"
//                + salt
//                + '\''
                + ", username='"
                + username
                + '\''
                + ", accountStatus='"
                + accountStatus
                + '\''
                + super.toString() +
                + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account that = (Account) o;
        return Objects.equals(id, that.id)
                //&& Objects.equals(active, that.active)
                && Objects.equals(createdBy, that.createdBy)
                && Objects.equals(createdOn, that.createdOn)
                && Objects.equals(email, that.email)
                && Objects.equals(modifiedBy, that.modifiedBy)
                && Objects.equals(modifiedOn, that.modifiedOn)
                && Objects.equals(password, that.password)
                //&& Objects.equals(salt, that.salt)
                && Objects.equals(username, that.username)
                && Objects.equals(accountStatus, that.accountStatus)
                && Objects.equals(version, that.version)
                ;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        //result = 31 * result + Objects.hashCode(active);
        result = 31 * result + Objects.hashCode(createdBy);
        result = 31 * result + Objects.hashCode(createdOn);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(modifiedBy);
        result = 31 * result + Objects.hashCode(modifiedOn);
        result = 31 * result + Objects.hashCode(password);
        //result = 31 * result + Objects.hashCode(salt);
        result = 31 * result + Objects.hashCode(username);
        result = 31 * result + Objects.hashCode(version);
        result = 31 * result + Objects.hashCode(accountStatus);

        return result;
    }

}
