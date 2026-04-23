package dev.sunusante.dmp.domain;

import static dev.sunusante.dmp.domain.MedicalEntryTestSamples.*;
import static dev.sunusante.dmp.domain.PrescriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import dev.sunusante.dmp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrescriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prescription.class);
        Prescription prescription1 = getPrescriptionSample1();
        Prescription prescription2 = new Prescription();
        assertThat(prescription1).isNotEqualTo(prescription2);

        prescription2.setId(prescription1.getId());
        assertThat(prescription1).isEqualTo(prescription2);

        prescription2 = getPrescriptionSample2();
        assertThat(prescription1).isNotEqualTo(prescription2);
    }

    @Test
    void entryTest() {
        Prescription prescription = getPrescriptionRandomSampleGenerator();
        MedicalEntry medicalEntryBack = getMedicalEntryRandomSampleGenerator();

        prescription.setEntry(medicalEntryBack);
        assertThat(prescription.getEntry()).isEqualTo(medicalEntryBack);

        prescription.entry(null);
        assertThat(prescription.getEntry()).isNull();
    }
}
