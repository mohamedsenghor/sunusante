package dev.sunusante.dmp.service.mapper;

import dev.sunusante.dmp.domain.MedicalEntry;
import dev.sunusante.dmp.service.dto.MedicalEntryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MedicalEntry} and its DTO {@link MedicalEntryDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicalEntryMapper extends EntityMapper<MedicalEntryDTO, MedicalEntry> {}
