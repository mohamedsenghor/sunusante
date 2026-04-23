package dev.sunusante.dmp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.sunusante.dmp.domain.enumeration.MedicalEntryCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A MedicalEntry.
 */
@Entity
@Table(name = "medical_entry")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "patient_pseudo", nullable = false)
    private String patientPseudo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private MedicalEntryCategory category;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "confidentiality_level")
    private Integer confidentialityLevel;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "entry")
    @JsonIgnoreProperties(value = { "entry" }, allowSetters = true)
    private Set<Prescription> prescriptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MedicalEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientPseudo() {
        return this.patientPseudo;
    }

    public MedicalEntry patientPseudo(String patientPseudo) {
        this.setPatientPseudo(patientPseudo);
        return this;
    }

    public void setPatientPseudo(String patientPseudo) {
        this.patientPseudo = patientPseudo;
    }

    public MedicalEntryCategory getCategory() {
        return this.category;
    }

    public MedicalEntry category(MedicalEntryCategory category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(MedicalEntryCategory category) {
        this.category = category;
    }

    public String getContent() {
        return this.content;
    }

    public MedicalEntry content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getConfidentialityLevel() {
        return this.confidentialityLevel;
    }

    public MedicalEntry confidentialityLevel(Integer confidentialityLevel) {
        this.setConfidentialityLevel(confidentialityLevel);
        return this;
    }

    public void setConfidentialityLevel(Integer confidentialityLevel) {
        this.confidentialityLevel = confidentialityLevel;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public MedicalEntry createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public MedicalEntry createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public MedicalEntry lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public MedicalEntry lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Prescription> getPrescriptions() {
        return this.prescriptions;
    }

    public void setPrescriptions(Set<Prescription> prescriptions) {
        if (this.prescriptions != null) {
            this.prescriptions.forEach(i -> i.setEntry(null));
        }
        if (prescriptions != null) {
            prescriptions.forEach(i -> i.setEntry(this));
        }
        this.prescriptions = prescriptions;
    }

    public MedicalEntry prescriptions(Set<Prescription> prescriptions) {
        this.setPrescriptions(prescriptions);
        return this;
    }

    public MedicalEntry addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
        prescription.setEntry(this);
        return this;
    }

    public MedicalEntry removePrescription(Prescription prescription) {
        this.prescriptions.remove(prescription);
        prescription.setEntry(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalEntry)) {
            return false;
        }
        return getId() != null && getId().equals(((MedicalEntry) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalEntry{" +
            "id=" + getId() +
            ", patientPseudo='" + getPatientPseudo() + "'" +
            ", category='" + getCategory() + "'" +
            ", content='" + getContent() + "'" +
            ", confidentialityLevel=" + getConfidentialityLevel() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
