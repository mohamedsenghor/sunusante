package dev.sunusante.patient.service.mapper;

import static dev.sunusante.patient.domain.LegalGuardianAsserts.*;
import static dev.sunusante.patient.domain.LegalGuardianTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LegalGuardianMapperTest {

    private LegalGuardianMapper legalGuardianMapper;

    @BeforeEach
    void setUp() {
        legalGuardianMapper = new LegalGuardianMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLegalGuardianSample1();
        var actual = legalGuardianMapper.toEntity(legalGuardianMapper.toDto(expected));
        assertLegalGuardianAllPropertiesEquals(expected, actual);
    }
}
