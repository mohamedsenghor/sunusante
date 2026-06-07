package dev.sunusante.patient.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sunusante.patient.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatientConsentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientConsentDTO.class);
        PatientConsentDTO patientConsentDTO1 = new PatientConsentDTO();
        patientConsentDTO1.setId(1L);
        PatientConsentDTO patientConsentDTO2 = new PatientConsentDTO();
        assertThat(patientConsentDTO1).isNotEqualTo(patientConsentDTO2);
        patientConsentDTO2.setId(patientConsentDTO1.getId());
        assertThat(patientConsentDTO1).isEqualTo(patientConsentDTO2);
        patientConsentDTO2.setId(2L);
        assertThat(patientConsentDTO1).isNotEqualTo(patientConsentDTO2);
        patientConsentDTO1.setId(null);
        assertThat(patientConsentDTO1).isNotEqualTo(patientConsentDTO2);
    }
}
