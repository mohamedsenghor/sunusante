package dev.sunusante.dmp.domain;

import static dev.sunusante.dmp.domain.MedicalEntryTestSamples.*;
import static dev.sunusante.dmp.domain.PrescriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import dev.sunusante.dmp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MedicalEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalEntry.class);
        MedicalEntry medicalEntry1 = getMedicalEntrySample1();
        MedicalEntry medicalEntry2 = new MedicalEntry();
        assertThat(medicalEntry1).isNotEqualTo(medicalEntry2);

        medicalEntry2.setId(medicalEntry1.getId());
        assertThat(medicalEntry1).isEqualTo(medicalEntry2);

        medicalEntry2 = getMedicalEntrySample2();
        assertThat(medicalEntry1).isNotEqualTo(medicalEntry2);
    }

    @Test
    void prescriptionTest() {
        MedicalEntry medicalEntry = getMedicalEntryRandomSampleGenerator();
        Prescription prescriptionBack = getPrescriptionRandomSampleGenerator();

        medicalEntry.addPrescription(prescriptionBack);
        assertThat(medicalEntry.getPrescriptions()).containsOnly(prescriptionBack);
        assertThat(prescriptionBack.getEntry()).isEqualTo(medicalEntry);

        medicalEntry.removePrescription(prescriptionBack);
        assertThat(medicalEntry.getPrescriptions()).doesNotContain(prescriptionBack);
        assertThat(prescriptionBack.getEntry()).isNull();

        medicalEntry.prescriptions(new HashSet<>(Set.of(prescriptionBack)));
        assertThat(medicalEntry.getPrescriptions()).containsOnly(prescriptionBack);
        assertThat(prescriptionBack.getEntry()).isEqualTo(medicalEntry);

        medicalEntry.setPrescriptions(new HashSet<>());
        assertThat(medicalEntry.getPrescriptions()).doesNotContain(prescriptionBack);
        assertThat(prescriptionBack.getEntry()).isNull();
    }
}
