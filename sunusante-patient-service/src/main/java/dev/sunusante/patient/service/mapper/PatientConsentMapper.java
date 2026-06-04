package dev.sunusante.patient.service.mapper;

import dev.sunusante.patient.domain.PatientConsent;
import dev.sunusante.patient.service.dto.PatientConsentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PatientConsent} and its DTO {@link PatientConsentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PatientConsentMapper extends EntityMapper<PatientConsentDTO, PatientConsent> {}
