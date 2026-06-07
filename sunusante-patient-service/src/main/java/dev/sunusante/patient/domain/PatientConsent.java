package dev.sunusante.patient.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.sunusante.patient.domain.enumeration.ConsentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Consentement pour le partage de dossier médical
 */
@Entity
@Table(name = "patient_consent")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientConsent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "doctor_login", nullable = false)
    private String doctorLogin;

    @Column(name = "consent_date")
    private Instant consentDate;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ConsentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "guardians", "consents" }, allowSetters = true)
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PatientConsent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoctorLogin() {
        return this.doctorLogin;
    }

    public PatientConsent doctorLogin(String doctorLogin) {
        this.setDoctorLogin(doctorLogin);
        return this;
    }

    public void setDoctorLogin(String doctorLogin) {
        this.doctorLogin = doctorLogin;
    }

    public Instant getConsentDate() {
        return this.consentDate;
    }

    public PatientConsent consentDate(Instant consentDate) {
        this.setConsentDate(consentDate);
        return this;
    }

    public void setConsentDate(Instant consentDate) {
        this.consentDate = consentDate;
    }

    public Instant getExpiryDate() {
        return this.expiryDate;
    }

    public PatientConsent expiryDate(Instant expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public ConsentStatus getStatus() {
        return this.status;
    }

    public PatientConsent status(ConsentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ConsentStatus status) {
        this.status = status;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public PatientConsent patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientConsent)) {
            return false;
        }
        return getId() != null && getId().equals(((PatientConsent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientConsent{" +
            "id=" + getId() +
            ", doctorLogin='" + getDoctorLogin() + "'" +
            ", consentDate='" + getConsentDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
