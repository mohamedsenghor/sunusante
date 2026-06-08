package dev.sunusante.dmp.service.mapper;

import static dev.sunusante.dmp.domain.PrescriptionAsserts.*;
import static dev.sunusante.dmp.domain.PrescriptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrescriptionMapperTest {

    private PrescriptionMapper prescriptionMapper;

    @BeforeEach
    void setUp() {
        prescriptionMapper = new PrescriptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPrescriptionSample1();
        var actual = prescriptionMapper.toEntity(prescriptionMapper.toDto(expected));
        assertPrescriptionAllPropertiesEquals(expected, actual);
    }
}
