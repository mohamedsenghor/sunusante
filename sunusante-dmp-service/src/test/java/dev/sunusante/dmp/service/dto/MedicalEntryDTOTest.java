package dev.sunusante.dmp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sunusante.dmp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicalEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalEntryDTO.class);
        MedicalEntryDTO medicalEntryDTO1 = new MedicalEntryDTO();
        medicalEntryDTO1.setId(1L);
        MedicalEntryDTO medicalEntryDTO2 = new MedicalEntryDTO();
        assertThat(medicalEntryDTO1).isNotEqualTo(medicalEntryDTO2);
        medicalEntryDTO2.setId(medicalEntryDTO1.getId());
        assertThat(medicalEntryDTO1).isEqualTo(medicalEntryDTO2);
        medicalEntryDTO2.setId(2L);
        assertThat(medicalEntryDTO1).isNotEqualTo(medicalEntryDTO2);
        medicalEntryDTO1.setId(null);
        assertThat(medicalEntryDTO1).isNotEqualTo(medicalEntryDTO2);
    }
}
