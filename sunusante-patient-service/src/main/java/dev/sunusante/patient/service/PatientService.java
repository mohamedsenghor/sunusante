package dev.sunusante.patient.service;

import dev.sunusante.patient.client.UserClient;
import dev.sunusante.patient.domain.Patient;
import dev.sunusante.patient.repository.PatientRepository;
import dev.sunusante.patient.service.dto.PatientDTO;
import dev.sunusante.patient.service.mapper.PatientMapper;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Patient}.
 */
@Service
@Transactional
public class PatientService {

    private static final Logger log = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;

    private final PatientMapper patientMapper;
    private final Random random;
    private final UserClient userClient;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper, Random random, UserClient userClient) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.random = random;
        this.userClient = userClient;
    }

    /**
     * Save a patient.
     *
     * @param patientDTO the entity to save.
     * @return the persisted entity.
     */
    public PatientDTO save(PatientDTO patientDTO) {
        log.debug("Request to save Patient : {}", patientDTO);
        if (!checkUserExistsByLogin(patientDTO.getLogin())) {
            throw new UsernameNotFoundException("Invalid login");
        }
        patientDTO.setPseudo(generatePseudo());
        Patient patient = patientMapper.toEntity(patientDTO);
        patient = patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    /**
     * Update a patient.
     *
     * @param patientDTO the entity to save.
     * @return the persisted entity.
     */
    public PatientDTO update(PatientDTO patientDTO) {
        log.debug("Request to update Patient : {}", patientDTO);
        Patient patient = patientMapper.toEntity(patientDTO);
        patient = patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    /**
     * Partially update a patient.
     *
     * @param patientDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PatientDTO> partialUpdate(PatientDTO patientDTO) {
        log.debug("Request to partially update Patient : {}", patientDTO);

        return patientRepository
            .findById(patientDTO.getId())
            .map(existingPatient -> {
                patientMapper.partialUpdate(existingPatient, patientDTO);

                return existingPatient;
            })
            .map(patientRepository::save)
            .map(patientMapper::toDto);
    }

    /**
     * Get all the patients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Patients");
        return patientRepository.findAll(pageable).map(patientMapper::toDto);
    }

    /**
     * Get one patient by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PatientDTO> findOne(Long id) {
        log.debug("Request to get Patient : {}", id);
        return patientRepository.findById(id).map(patientMapper::toDto);
    }

    /**
     * Get one patient by pseudo.
     *
     * @param pseudo the pseudo of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PatientDTO> findOneByPseudo(String pseudo) {
        log.debug("Request to get Patient by pseudo : {}", pseudo);
        return patientRepository.findOneByPseudo(pseudo).map(patientMapper::toDto);
    }

    /**
     * Delete the patient by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Patient : {}", id);
        patientRepository.deleteById(id);
    }

    /**
     * Check if a user exists in the Gateway by login.
     *
     * @param login the login of the user.
     * @return true if user exists, false otherwise.
     */
    @Transactional(readOnly = true)
    public boolean checkUserExistsByLogin(String login) {
        log.debug("Request to check if user exists in Gateway : {}", login);
        return Boolean.TRUE.equals(userClient.checkUserExists(login));
    }

    private String generatePseudo() {
        // Use 12 characters alpanumeric string
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder pseudo = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            pseudo.append(chars.charAt(random.nextInt(chars.length())));
        }
        return pseudo.toString();
    }
}
