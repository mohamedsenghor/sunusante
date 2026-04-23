package dev.sunusante.dmp.service;

import dev.sunusante.dmp.domain.MedicalEntry;
import dev.sunusante.dmp.repository.MedicalEntryRepository;
import dev.sunusante.dmp.service.dto.MedicalEntryDTO;
import dev.sunusante.dmp.service.mapper.MedicalEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link dev.sunusante.dmp.domain.MedicalEntry}.
 */
@Service
@Transactional
public class MedicalEntryService {

    private static final Logger log = LoggerFactory.getLogger(MedicalEntryService.class);

    private final MedicalEntryRepository medicalEntryRepository;

    private final MedicalEntryMapper medicalEntryMapper;

    public MedicalEntryService(MedicalEntryRepository medicalEntryRepository, MedicalEntryMapper medicalEntryMapper) {
        this.medicalEntryRepository = medicalEntryRepository;
        this.medicalEntryMapper = medicalEntryMapper;
    }

    /**
     * Save a medicalEntry.
     *
     * @param medicalEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicalEntryDTO save(MedicalEntryDTO medicalEntryDTO) {
        log.debug("Request to save MedicalEntry : {}", medicalEntryDTO);
        MedicalEntry medicalEntry = medicalEntryMapper.toEntity(medicalEntryDTO);
        medicalEntry = medicalEntryRepository.save(medicalEntry);
        return medicalEntryMapper.toDto(medicalEntry);
    }

    /**
     * Update a medicalEntry.
     *
     * @param medicalEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicalEntryDTO update(MedicalEntryDTO medicalEntryDTO) {
        log.debug("Request to update MedicalEntry : {}", medicalEntryDTO);
        MedicalEntry medicalEntry = medicalEntryMapper.toEntity(medicalEntryDTO);
        medicalEntry = medicalEntryRepository.save(medicalEntry);
        return medicalEntryMapper.toDto(medicalEntry);
    }

    /**
     * Partially update a medicalEntry.
     *
     * @param medicalEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicalEntryDTO> partialUpdate(MedicalEntryDTO medicalEntryDTO) {
        log.debug("Request to partially update MedicalEntry : {}", medicalEntryDTO);

        return medicalEntryRepository
            .findById(medicalEntryDTO.getId())
            .map(existingMedicalEntry -> {
                medicalEntryMapper.partialUpdate(existingMedicalEntry, medicalEntryDTO);

                return existingMedicalEntry;
            })
            .map(medicalEntryRepository::save)
            .map(medicalEntryMapper::toDto);
    }

    /**
     * Get all the medicalEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicalEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MedicalEntries");
        return medicalEntryRepository.findAll(pageable).map(medicalEntryMapper::toDto);
    }

    /**
     * Get one medicalEntry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicalEntryDTO> findOne(Long id) {
        log.debug("Request to get MedicalEntry : {}", id);
        return medicalEntryRepository.findById(id).map(medicalEntryMapper::toDto);
    }

    /**
     * Delete the medicalEntry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MedicalEntry : {}", id);
        medicalEntryRepository.deleteById(id);
    }
}
