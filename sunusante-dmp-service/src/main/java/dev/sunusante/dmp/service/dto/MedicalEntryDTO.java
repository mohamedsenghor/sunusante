package dev.sunusante.dmp.service.dto;

import dev.sunusante.dmp.domain.enumeration.MedicalEntryCategory;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link dev.sunusante.dmp.domain.MedicalEntry} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalEntryDTO implements Serializable {

    private Long id;

    @NotNull
    private String patientPseudo;

    @NotNull
    private MedicalEntryCategory category;

    @Lob
    private String content;

    private Integer confidentialityLevel;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientPseudo() {
        return patientPseudo;
    }

    public void setPatientPseudo(String patientPseudo) {
        this.patientPseudo = patientPseudo;
    }

    public MedicalEntryCategory getCategory() {
        return category;
    }

    public void setCategory(MedicalEntryCategory category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getConfidentialityLevel() {
        return confidentialityLevel;
    }

    public void setConfidentialityLevel(Integer confidentialityLevel) {
        this.confidentialityLevel = confidentialityLevel;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalEntryDTO)) {
            return false;
        }

        MedicalEntryDTO medicalEntryDTO = (MedicalEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicalEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalEntryDTO{" +
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
