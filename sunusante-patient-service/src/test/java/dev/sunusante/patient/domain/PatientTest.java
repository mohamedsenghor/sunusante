package dev.sunusante.patient.domain;

import static dev.sunusante.patient.domain.LegalGuardianTestSamples.*;
import static dev.sunusante.patient.domain.PatientConsentTestSamples.*;
import static dev.sunusante.patient.domain.PatientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import dev.sunusante.patient.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PatientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Patient.class);
        Patient patient1 = getPatientSample1();
        Patient patient2 = new Patient();
        assertThat(patient1).isNotEqualTo(patient2);

        patient2.setId(patient1.getId());
        assertThat(patient1).isEqualTo(patient2);

        patient2 = getPatientSample2();
        assertThat(patient1).isNotEqualTo(patient2);
    }

    @Test
    void guardianTest() {
        Patient patient = getPatientRandomSampleGenerator();
        LegalGuardian legalGuardianBack = getLegalGuardianRandomSampleGenerator();

        patient.addGuardian(legalGuardianBack);
        assertThat(patient.getGuardians()).containsOnly(legalGuardianBack);
        assertThat(legalGuardianBack.getDependent()).isEqualTo(patient);

        patient.removeGuardian(legalGuardianBack);
        assertThat(patient.getGuardians()).doesNotContain(legalGuardianBack);
        assertThat(legalGuardianBack.getDependent()).isNull();

        patient.guardians(new HashSet<>(Set.of(legalGuardianBack)));
        assertThat(patient.getGuardians()).containsOnly(legalGuardianBack);
        assertThat(legalGuardianBack.getDependent()).isEqualTo(patient);

        patient.setGuardians(new HashSet<>());
        assertThat(patient.getGuardians()).doesNotContain(legalGuardianBack);
        assertThat(legalGuardianBack.getDependent()).isNull();
    }

    @Test
    void consentTest() {
        Patient patient = getPatientRandomSampleGenerator();
        PatientConsent patientConsentBack = getPatientConsentRandomSampleGenerator();

        patient.addConsent(patientConsentBack);
        assertThat(patient.getConsents()).containsOnly(patientConsentBack);
        assertThat(patientConsentBack.getPatient()).isEqualTo(patient);

        patient.removeConsent(patientConsentBack);
        assertThat(patient.getConsents()).doesNotContain(patientConsentBack);
        assertThat(patientConsentBack.getPatient()).isNull();

        patient.consents(new HashSet<>(Set.of(patientConsentBack)));
        assertThat(patient.getConsents()).containsOnly(patientConsentBack);
        assertThat(patientConsentBack.getPatient()).isEqualTo(patient);

        patient.setConsents(new HashSet<>());
        assertThat(patient.getConsents()).doesNotContain(patientConsentBack);
        assertThat(patientConsentBack.getPatient()).isNull();
    }
}
