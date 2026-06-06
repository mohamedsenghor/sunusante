package dev.sunusante.patient.service.mapper;

import dev.sunusante.patient.domain.Patient;
import dev.sunusante.patient.domain.PatientConsent;
import dev.sunusante.patient.service.dto.PatientConsentDTO;
import dev.sunusante.patient.service.dto.PatientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PatientConsent} and its DTO {@link PatientConsentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PatientConsentMapper extends EntityMapper<PatientConsentDTO, PatientConsent> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientId")
    PatientConsentDTO toDto(PatientConsent s);

    @Named("patientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);
}
