package dev.sunusante.patient.service.mapper;

import dev.sunusante.patient.domain.Patient;
import dev.sunusante.patient.service.dto.PatientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Patient} and its DTO {@link PatientDTO}.
 */
@Mapper(componentModel = "spring")
public interface PatientMapper extends EntityMapper<PatientDTO, Patient> {}
