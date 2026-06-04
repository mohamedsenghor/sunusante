package dev.sunusante.patient.service.dto;

import dev.sunusante.patient.domain.enumeration.ConsentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link dev.sunusante.patient.domain.PatientConsent} entity.
 */
@Schema(description = "Consentement pour le partage de dossier médical")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientConsentDTO implements Serializable {

    private Long id;

    @NotNull
    private String patientPseudo;

    @NotNull
    private String doctorLogin;

    private Instant consentDate;

    private Instant expiryDate;

    private ConsentStatus status;

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

    public String getDoctorLogin() {
        return doctorLogin;
    }

    public void setDoctorLogin(String doctorLogin) {
        this.doctorLogin = doctorLogin;
    }

    public Instant getConsentDate() {
        return consentDate;
    }

    public void setConsentDate(Instant consentDate) {
        this.consentDate = consentDate;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public ConsentStatus getStatus() {
        return status;
    }

    public void setStatus(ConsentStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientConsentDTO)) {
            return false;
        }

        PatientConsentDTO patientConsentDTO = (PatientConsentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, patientConsentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientConsentDTO{" +
            "id=" + getId() +
            ", patientPseudo='" + getPatientPseudo() + "'" +
            ", doctorLogin='" + getDoctorLogin() + "'" +
            ", consentDate='" + getConsentDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
