package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


/**
 * Audit fields are used by the application to track who/when an object is created or modified.
 * This could be a user or a system process.
 * @author mgabelmann
 */
@MappedSuperclass
public abstract class AbstractAuditable implements Serializable {
    @Column(name = "CREATEDBY", nullable = false, updatable = false)
    protected UUID createdBy;

    @Column(name = "MODIFIEDBY", nullable = false)
    protected UUID modifiedBy;

    @Column(name = "CREATEDON_DTM", nullable = false, updatable = false)
    protected Instant createdOn;

    @Column(name = "MODIFIEDON_DTM", nullable = false)
    protected Instant modifiedOn;

    @Version
    @Column(name = "VERSION", nullable = false)
    protected Long version;


    /** Constructor. */
    protected AbstractAuditable() {
        ;
    }

    /**
     * Constructor.
     * @param createdOn created on
     * @param createdBy created by
     * @param modifiedOn modified on
     * @param modifiedBy modified by
     * @param version version
     */
    public AbstractAuditable(final UUID createdBy, final Instant createdOn, final UUID modifiedBy, final Instant modifiedOn, final Long version) {
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.version = version;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final UUID createdBy) {
        this.createdBy = createdBy;
    }

    public UUID getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(final UUID modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(final Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(final Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

}
