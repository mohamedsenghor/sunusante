package dev.sunusante.dmp.service.mapper;

import static dev.sunusante.dmp.domain.MedicalEntryAsserts.*;
import static dev.sunusante.dmp.domain.MedicalEntryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicalEntryMapperTest {

    private MedicalEntryMapper medicalEntryMapper;

    @BeforeEach
    void setUp() {
        medicalEntryMapper = new MedicalEntryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicalEntrySample1();
        var actual = medicalEntryMapper.toEntity(medicalEntryMapper.toDto(expected));
        assertMedicalEntryAllPropertiesEquals(expected, actual);
    }
}
