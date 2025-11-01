package ca.mikegabelmann.judgement.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import java.io.Serializable;
import java.time.Instant;


/**
 * Audit fields are used by the application to track who/when an object is created or modified.
 * This could be a user or a system process.
 * @author mgabelmann
 */
@MappedSuperclass
public abstract class AbstractAuditable implements Serializable {
    @Column(name = "CREATEDBY", nullable = false, updatable = false, length = 16)
    protected String createdBy;

    @Column(name = "MODIFIEDBY", nullable = false, length = 16)
    protected String modifiedBy;

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
    public AbstractAuditable(final String createdBy, final Instant createdOn, final String modifiedBy, final Instant modifiedOn, final Long version) {
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.version = version;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(final String modifiedBy) {
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

    @Override
    public String toString() {
        return  ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", createdOn=" + createdOn +
                ", modifiedOn=" + modifiedOn +
                ", version=" + version
                ;
    }

}
