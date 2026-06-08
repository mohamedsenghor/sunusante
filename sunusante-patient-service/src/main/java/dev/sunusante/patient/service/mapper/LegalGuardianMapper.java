package dev.sunusante.patient.service.mapper;

import dev.sunusante.patient.domain.LegalGuardian;
import dev.sunusante.patient.domain.Patient;
import dev.sunusante.patient.service.dto.LegalGuardianDTO;
import dev.sunusante.patient.service.dto.PatientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LegalGuardian} and its DTO {@link LegalGuardianDTO}.
 */
@Mapper(componentModel = "spring")
public interface LegalGuardianMapper extends EntityMapper<LegalGuardianDTO, LegalGuardian> {
    @Mapping(target = "dependent", source = "dependent", qualifiedByName = "patientId")
    LegalGuardianDTO toDto(LegalGuardian s);

    @Named("patientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);
}
