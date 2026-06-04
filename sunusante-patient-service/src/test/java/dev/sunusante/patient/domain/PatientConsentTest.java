package dev.sunusante.patient.domain;

import static dev.sunusante.patient.domain.PatientConsentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import dev.sunusante.patient.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatientConsentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientConsent.class);
        PatientConsent patientConsent1 = getPatientConsentSample1();
        PatientConsent patientConsent2 = new PatientConsent();
        assertThat(patientConsent1).isNotEqualTo(patientConsent2);

        patientConsent2.setId(patientConsent1.getId());
        assertThat(patientConsent1).isEqualTo(patientConsent2);

        patientConsent2 = getPatientConsentSample2();
        assertThat(patientConsent1).isNotEqualTo(patientConsent2);
    }
}
