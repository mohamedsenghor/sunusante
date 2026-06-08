package dev.sunusante.patient.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sunusante.patient.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LegalGuardianDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LegalGuardianDTO.class);
        LegalGuardianDTO legalGuardianDTO1 = new LegalGuardianDTO();
        legalGuardianDTO1.setId(1L);
        LegalGuardianDTO legalGuardianDTO2 = new LegalGuardianDTO();
        assertThat(legalGuardianDTO1).isNotEqualTo(legalGuardianDTO2);
        legalGuardianDTO2.setId(legalGuardianDTO1.getId());
        assertThat(legalGuardianDTO1).isEqualTo(legalGuardianDTO2);
        legalGuardianDTO2.setId(2L);
        assertThat(legalGuardianDTO1).isNotEqualTo(legalGuardianDTO2);
        legalGuardianDTO1.setId(null);
        assertThat(legalGuardianDTO1).isNotEqualTo(legalGuardianDTO2);
    }
}
