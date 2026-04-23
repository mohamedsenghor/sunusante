package dev.sunusante.patient.service;

import dev.sunusante.patient.domain.LegalGuardian;
import dev.sunusante.patient.repository.LegalGuardianRepository;
import dev.sunusante.patient.service.dto.LegalGuardianDTO;
import dev.sunusante.patient.service.mapper.LegalGuardianMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link dev.sunusante.patient.domain.LegalGuardian}.
 */
@Service
@Transactional
public class LegalGuardianService {

    private static final Logger log = LoggerFactory.getLogger(LegalGuardianService.class);

    private final LegalGuardianRepository legalGuardianRepository;

    private final LegalGuardianMapper legalGuardianMapper;

    public LegalGuardianService(LegalGuardianRepository legalGuardianRepository, LegalGuardianMapper legalGuardianMapper) {
        this.legalGuardianRepository = legalGuardianRepository;
        this.legalGuardianMapper = legalGuardianMapper;
    }

    /**
     * Save a legalGuardian.
     *
     * @param legalGuardianDTO the entity to save.
     * @return the persisted entity.
     */
    public LegalGuardianDTO save(LegalGuardianDTO legalGuardianDTO) {
        log.debug("Request to save LegalGuardian : {}", legalGuardianDTO);
        LegalGuardian legalGuardian = legalGuardianMapper.toEntity(legalGuardianDTO);
        legalGuardian = legalGuardianRepository.save(legalGuardian);
        return legalGuardianMapper.toDto(legalGuardian);
    }

    /**
     * Update a legalGuardian.
     *
     * @param legalGuardianDTO the entity to save.
     * @return the persisted entity.
     */
    public LegalGuardianDTO update(LegalGuardianDTO legalGuardianDTO) {
        log.debug("Request to update LegalGuardian : {}", legalGuardianDTO);
        LegalGuardian legalGuardian = legalGuardianMapper.toEntity(legalGuardianDTO);
        legalGuardian = legalGuardianRepository.save(legalGuardian);
        return legalGuardianMapper.toDto(legalGuardian);
    }

    /**
     * Partially update a legalGuardian.
     *
     * @param legalGuardianDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LegalGuardianDTO> partialUpdate(LegalGuardianDTO legalGuardianDTO) {
        log.debug("Request to partially update LegalGuardian : {}", legalGuardianDTO);

        return legalGuardianRepository
            .findById(legalGuardianDTO.getId())
            .map(existingLegalGuardian -> {
                legalGuardianMapper.partialUpdate(existingLegalGuardian, legalGuardianDTO);

                return existingLegalGuardian;
            })
            .map(legalGuardianRepository::save)
            .map(legalGuardianMapper::toDto);
    }

    /**
     * Get all the legalGuardians.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LegalGuardianDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LegalGuardians");
        return legalGuardianRepository.findAll(pageable).map(legalGuardianMapper::toDto);
    }

    /**
     * Get one legalGuardian by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LegalGuardianDTO> findOne(Long id) {
        log.debug("Request to get LegalGuardian : {}", id);
        return legalGuardianRepository.findById(id).map(legalGuardianMapper::toDto);
    }

    /**
     * Delete the legalGuardian by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LegalGuardian : {}", id);
        legalGuardianRepository.deleteById(id);
    }
}
