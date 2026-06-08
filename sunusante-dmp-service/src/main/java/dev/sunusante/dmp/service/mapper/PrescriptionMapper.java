package dev.sunusante.dmp.service.mapper;

import dev.sunusante.dmp.domain.MedicalEntry;
import dev.sunusante.dmp.domain.Prescription;
import dev.sunusante.dmp.service.dto.MedicalEntryDTO;
import dev.sunusante.dmp.service.dto.PrescriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Prescription} and its DTO {@link PrescriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PrescriptionMapper extends EntityMapper<PrescriptionDTO, Prescription> {
    @Mapping(target = "entry", source = "entry", qualifiedByName = "medicalEntryId")
    PrescriptionDTO toDto(Prescription s);

    @Named("medicalEntryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicalEntryDTO toDtoMedicalEntryId(MedicalEntry medicalEntry);
}
