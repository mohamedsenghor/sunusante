package dev.sunusante.patient.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.sunusante.patient.domain.enumeration.IdentifierType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Patient Microservice (PII Storage)
 * Gère les données d'identification du patient
 */
@Entity
@Table(name = "patient")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "pseudo", unique = true)
    private String pseudo;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "id_type")
    private IdentifierType idType;

    @Column(name = "id_value")
    private String idValue;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dependent")
    @JsonIgnoreProperties(value = { "dependent" }, allowSetters = true)
    private Set<LegalGuardian> guardians = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<PatientConsent> consents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Patient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public Patient login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public Patient pseudo(String pseudo) {
        this.setPseudo(pseudo);
        return this;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Patient firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Patient lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Patient email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Patient birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public IdentifierType getIdType() {
        return this.idType;
    }

    public Patient idType(IdentifierType idType) {
        this.setIdType(idType);
        return this;
    }

    public void setIdType(IdentifierType idType) {
        this.idType = idType;
    }

    public String getIdValue() {
        return this.idValue;
    }

    public Patient idValue(String idValue) {
        this.setIdValue(idValue);
        return this;
    }

    public void setIdValue(String idValue) {
        this.idValue = idValue;
    }

    public Set<LegalGuardian> getGuardians() {
        return this.guardians;
    }

    public void setGuardians(Set<LegalGuardian> legalGuardians) {
        if (this.guardians != null) {
            this.guardians.forEach(i -> i.setDependent(null));
        }
        if (legalGuardians != null) {
            legalGuardians.forEach(i -> i.setDependent(this));
        }
        this.guardians = legalGuardians;
    }

    public Patient guardians(Set<LegalGuardian> legalGuardians) {
        this.setGuardians(legalGuardians);
        return this;
    }

    public Patient addGuardian(LegalGuardian legalGuardian) {
        this.guardians.add(legalGuardian);
        legalGuardian.setDependent(this);
        return this;
    }

    public Patient removeGuardian(LegalGuardian legalGuardian) {
        this.guardians.remove(legalGuardian);
        legalGuardian.setDependent(null);
        return this;
    }

    public Set<PatientConsent> getConsents() {
        return this.consents;
    }

    public void setConsents(Set<PatientConsent> patientConsents) {
        if (this.consents != null) {
            this.consents.forEach(i -> i.setPatient(null));
        }
        if (patientConsents != null) {
            patientConsents.forEach(i -> i.setPatient(this));
        }
        this.consents = patientConsents;
    }

    public Patient consents(Set<PatientConsent> patientConsents) {
        this.setConsents(patientConsents);
        return this;
    }

    public Patient addConsent(PatientConsent patientConsent) {
        this.consents.add(patientConsent);
        patientConsent.setPatient(this);
        return this;
    }

    public Patient removeConsent(PatientConsent patientConsent) {
        this.consents.remove(patientConsent);
        patientConsent.setPatient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return getId() != null && getId().equals(((Patient) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", pseudo='" + getPseudo() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", idType='" + getIdType() + "'" +
            ", idValue='" + getIdValue() + "'" +
            "}";
    }
}
