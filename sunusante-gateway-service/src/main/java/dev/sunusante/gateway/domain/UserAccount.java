package dev.sunusante.gateway.domain;
// Destination
import dev.sunusante.gateway.domain.enumeration.UserRole;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * IAM - Identity Management
 * Application du principe de Moindre Privilège
 */
@Table("user_account")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("mfa_secret")
    private String mfaSecret;

    @Column("role")
    private UserRole role;

    @Column("created_by")
    private String createdBy;

    @Column("created_date")
    private Instant createdDate;

    @Column("last_modified_by")
    private String lastModifiedBy;

    @Column("last_modified_date")
    private Instant lastModifiedDate;

    @Transient
    private User internalUser;

    @Column("internal_user_id")
    private Long internalUserId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMfaSecret() {
        return this.mfaSecret;
    }

    public UserAccount mfaSecret(String mfaSecret) {
        this.setMfaSecret(mfaSecret);
        return this;
    }

    public void setMfaSecret(String mfaSecret) {
        this.mfaSecret = mfaSecret;
    }

    public UserRole getRole() {
        return this.role;
    }

    public UserAccount role(UserRole role) {
        this.setRole(role);
        return this;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserAccount createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserAccount createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserAccount lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserAccount lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
        this.internalUserId = user != null ? user.getId() : null;
    }

    public UserAccount internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public Long getInternalUserId() {
        return this.internalUserId;
    }

    public void setInternalUserId(Long user) {
        this.internalUserId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccount)) {
            return false;
        }
        return getId() != null && getId().equals(((UserAccount) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccount{" +
            "id=" + getId() +
            ", mfaSecret='" + getMfaSecret() + "'" +
            ", role='" + getRole() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
