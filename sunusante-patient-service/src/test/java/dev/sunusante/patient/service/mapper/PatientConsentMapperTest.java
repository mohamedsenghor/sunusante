package dev.sunusante.patient.service.mapper;

import static dev.sunusante.patient.domain.PatientConsentAsserts.*;
import static dev.sunusante.patient.domain.PatientConsentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatientConsentMapperTest {

    private PatientConsentMapper patientConsentMapper;

    @BeforeEach
    void setUp() {
        patientConsentMapper = new PatientConsentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPatientConsentSample1();
        var actual = patientConsentMapper.toEntity(patientConsentMapper.toDto(expected));
        assertPatientConsentAllPropertiesEquals(expected, actual);
    }
}
