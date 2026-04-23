package dev.sunusante.patient.domain;

import static dev.sunusante.patient.domain.LegalGuardianTestSamples.*;
import static dev.sunusante.patient.domain.PatientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import dev.sunusante.patient.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LegalGuardianTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LegalGuardian.class);
        LegalGuardian legalGuardian1 = getLegalGuardianSample1();
        LegalGuardian legalGuardian2 = new LegalGuardian();
        assertThat(legalGuardian1).isNotEqualTo(legalGuardian2);

        legalGuardian2.setId(legalGuardian1.getId());
        assertThat(legalGuardian1).isEqualTo(legalGuardian2);

        legalGuardian2 = getLegalGuardianSample2();
        assertThat(legalGuardian1).isNotEqualTo(legalGuardian2);
    }

    @Test
    void dependentTest() {
        LegalGuardian legalGuardian = getLegalGuardianRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        legalGuardian.setDependent(patientBack);
        assertThat(legalGuardian.getDependent()).isEqualTo(patientBack);

        legalGuardian.dependent(null);
        assertThat(legalGuardian.getDependent()).isNull();
    }
}
