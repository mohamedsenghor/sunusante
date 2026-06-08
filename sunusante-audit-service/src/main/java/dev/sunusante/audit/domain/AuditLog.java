package dev.sunusante.audit.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Traçabilité complète exigée par HDS/RGPD
 */
@Entity
@Table(name = "audit_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @NotNull
    @Column(name = "principal", nullable = false)
    private String principal;

    @NotNull
    @Column(name = "resource_id", nullable = false)
    private String resourceId;

    @NotNull
    @Column(name = "action", nullable = false)
    private String action;

    @Lob
    @Column(name = "old_value")
    private String oldValue;

    @Lob
    @Column(name = "new_value")
    private String newValue;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "is_success")
    private Boolean isSuccess;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AuditLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public AuditLog timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrincipal() {
        return this.principal;
    }

    public AuditLog principal(String principal) {
        this.setPrincipal(principal);
        return this;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getResourceId() {
        return this.resourceId;
    }

    public AuditLog resourceId(String resourceId) {
        this.setResourceId(resourceId);
        return this;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getAction() {
        return this.action;
    }

    public AuditLog action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOldValue() {
        return this.oldValue;
    }

    public AuditLog oldValue(String oldValue) {
        this.setOldValue(oldValue);
        return this;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return this.newValue;
    }

    public AuditLog newValue(String newValue) {
        this.setNewValue(newValue);
        return this;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getClientIp() {
        return this.clientIp;
    }

    public AuditLog clientIp(String clientIp) {
        this.setClientIp(clientIp);
        return this;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Boolean getIsSuccess() {
        return this.isSuccess;
    }

    public AuditLog isSuccess(Boolean isSuccess) {
        this.setIsSuccess(isSuccess);
        return this;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public AuditLog createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public AuditLog createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public AuditLog lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public AuditLog lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditLog)) {
            return false;
        }
        return getId() != null && getId().equals(((AuditLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditLog{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", principal='" + getPrincipal() + "'" +
            ", resourceId='" + getResourceId() + "'" +
            ", action='" + getAction() + "'" +
            ", oldValue='" + getOldValue() + "'" +
            ", newValue='" + getNewValue() + "'" +
            ", clientIp='" + getClientIp() + "'" +
            ", isSuccess='" + getIsSuccess() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
