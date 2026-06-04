package dev.sunusante.patient.service;

import dev.sunusante.patient.broker.NotificationProducer;
import dev.sunusante.patient.client.AuditLogClient;
import dev.sunusante.patient.domain.PatientConsent;
import dev.sunusante.patient.domain.enumeration.ConsentStatus;
import dev.sunusante.patient.repository.PatientConsentRepository;
import dev.sunusante.patient.security.SecurityUtils;
import dev.sunusante.patient.service.dto.PatientConsentDTO;
import dev.sunusante.patient.service.mapper.PatientConsentMapper;
import dev.sunusante.patient.web.rest.errors.BadRequestAlertException;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link dev.sunusante.patient.domain.PatientConsent}.
 */
@Service
@Transactional
public class PatientConsentService {

    private static final Logger log = LoggerFactory.getLogger(PatientConsentService.class);

    private final PatientConsentRepository patientConsentRepository;

    private final PatientConsentMapper patientConsentMapper;

    private final AuditLogClient auditLogClient;

    private final NotificationProducer notificationProducer;

    private final dev.sunusante.patient.repository.PatientRepository patientRepository;

    public PatientConsentService(
        PatientConsentRepository patientConsentRepository,
        PatientConsentMapper patientConsentMapper,
        AuditLogClient auditLogClient,
        NotificationProducer notificationProducer,
        dev.sunusante.patient.repository.PatientRepository patientRepository
    ) {
        this.patientConsentRepository = patientConsentRepository;
        this.patientConsentMapper = patientConsentMapper;
        this.auditLogClient = auditLogClient;
        this.notificationProducer = notificationProducer;
        this.patientRepository = patientRepository;
    }

    /**
     * Save a patientConsent.
     *
     * @param patientConsentDTO the entity to save.
     * @return the persisted entity.
     */
    public PatientConsentDTO save(PatientConsentDTO patientConsentDTO) {
        log.debug("Request to save PatientConsent : {}", patientConsentDTO);
        PatientConsent patientConsent = patientConsentMapper.toEntity(patientConsentDTO);
        patientConsent = patientConsentRepository.save(patientConsent);

        String email = patientRepository.findOneByPseudo(patientConsent.getPatientPseudo())
            .map(dev.sunusante.patient.domain.Patient::getEmail)
            .orElse(null);

        notificationProducer.sendNotification(
            patientConsent.getPatientPseudo(),
            email,
            "Un médecin a demandé l'accès à votre dossier médical. Veuillez valider la demande."
        );

        return patientConsentMapper.toDto(patientConsent);
    }

    /**
     * Update a patientConsent.
     *
     * @param patientConsentDTO the entity to save.
     * @return the persisted entity.
     */
    public PatientConsentDTO update(PatientConsentDTO patientConsentDTO) {
        log.debug("Request to update PatientConsent : {}", patientConsentDTO);
        PatientConsent patientConsent = patientConsentMapper.toEntity(patientConsentDTO);
        patientConsent = patientConsentRepository.save(patientConsent);
        return patientConsentMapper.toDto(patientConsent);
    }

    /**
     * Partially update a patientConsent.
     *
     * @param patientConsentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PatientConsentDTO> partialUpdate(PatientConsentDTO patientConsentDTO) {
        log.debug("Request to partially update PatientConsent : {}", patientConsentDTO);

        return patientConsentRepository
            .findById(patientConsentDTO.getId())
            .map(existingPatientConsent -> {
                patientConsentMapper.partialUpdate(existingPatientConsent, patientConsentDTO);

                return existingPatientConsent;
            })
            .map(patientConsentRepository::save)
            .map(patientConsentMapper::toDto);
    }

    /**
     * Get all the patientConsents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientConsentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PatientConsents");
        return patientConsentRepository.findAll(pageable).map(patientConsentMapper::toDto);
    }

    /**
     * Get one patientConsent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PatientConsentDTO> findOne(Long id) {
        log.debug("Request to get PatientConsent : {}", id);
        return patientConsentRepository.findById(id).map(patientConsentMapper::toDto);
    }

    /**
     * Delete the patientConsent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PatientConsent : {}", id);
        patientConsentRepository.deleteById(id);
    }

    /**
     * Check if a doctor has an active consent for a patient record.
     *
     * @param patientPseudo the pseudo of the patient.
     * @param doctorLogin the login of the doctor.
     * @return true if an active consent exists, false otherwise.
     */
    @Transactional(readOnly = true)
    public boolean hasActiveConsent(String patientPseudo, String doctorLogin) {
        log.debug("Request to check active consent for patient {} and doctor {}", patientPseudo, doctorLogin);
        return patientConsentRepository
            .findFirstByPatientPseudoAndDoctorLoginAndStatusAndExpiryDateAfterOrderByConsentDateDesc(
                patientPseudo,
                doctorLogin,
                ConsentStatus.ACTIVE,
                Instant.now()
            )
            .isPresent();
    }

    /**
     * Approve a pending patient consent.
     *
     * @param id the id of the entity.
     * @return the persisted entity.
     */
    public PatientConsentDTO approve(Long id) {
        log.debug("Request to approve PatientConsent : {}", id);
        return patientConsentRepository
            .findById(id)
            .map(patientConsent -> {
                patientConsent.setStatus(ConsentStatus.ACTIVE);
                patientConsent.setConsentDate(Instant.now());
                patientConsent.setExpiryDate(Instant.now().plus(java.time.Duration.ofDays(365))); // Default 1 year
                PatientConsent updated = patientConsentRepository.save(patientConsent);

                // Audit Log
                AuditLogClient.AuditLogDTO auditLog = new AuditLogClient.AuditLogDTO();
                auditLog.setTimestamp(Instant.now());
                auditLog.setPrincipal(SecurityUtils.getCurrentUserLogin().orElse("anonymous"));
                auditLog.setResourceId("PatientConsent:" + id);
                auditLog.setAction("APPROVE");
                auditLog.setIsSuccess(true);
                try {
                    auditLogClient.createAuditLog(auditLog);
                } catch (Exception e) {
                    log.error("Failed to send audit log", e);
                }

                return updated;
            })
            .map(patientConsentMapper::toDto)
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "PatientConsent", "idnotfound"));
    }
}
