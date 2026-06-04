package dev.sunusante.dmp.service;

import dev.sunusante.dmp.client.AuditLogClient;
import dev.sunusante.dmp.client.PatientConsentClient;
import dev.sunusante.dmp.domain.Prescription;
import dev.sunusante.dmp.repository.PrescriptionRepository;
import dev.sunusante.dmp.security.SecurityUtils;
import dev.sunusante.dmp.service.dto.PrescriptionDTO;
import dev.sunusante.dmp.service.mapper.PrescriptionMapper;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link dev.sunusante.dmp.domain.Prescription}.
 */
@Service
@Transactional
public class PrescriptionService {

    private static final Logger log = LoggerFactory.getLogger(PrescriptionService.class);

    private final PrescriptionRepository prescriptionRepository;

    private final PrescriptionMapper prescriptionMapper;

    private final PatientConsentClient patientConsentClient;

    private final AuditLogClient auditLogClient;

    public PrescriptionService(
        PrescriptionRepository prescriptionRepository,
        PrescriptionMapper prescriptionMapper,
        PatientConsentClient patientConsentClient,
        AuditLogClient auditLogClient
    ) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionMapper = prescriptionMapper;
        this.patientConsentClient = patientConsentClient;
        this.auditLogClient = auditLogClient;
    }

    /**
     * Save a prescription.
     *
     * @param prescriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public PrescriptionDTO save(PrescriptionDTO prescriptionDTO) {
        log.debug("Request to save Prescription : {}", prescriptionDTO);
        Prescription prescription = prescriptionMapper.toEntity(prescriptionDTO);
        prescription = prescriptionRepository.save(prescription);
        return prescriptionMapper.toDto(prescription);
    }

    /**
     * Update a prescription.
     *
     * @param prescriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public PrescriptionDTO update(PrescriptionDTO prescriptionDTO) {
        log.debug("Request to update Prescription : {}", prescriptionDTO);
        Prescription prescription = prescriptionMapper.toEntity(prescriptionDTO);
        prescription = prescriptionRepository.save(prescription);
        return prescriptionMapper.toDto(prescription);
    }

    /**
     * Partially update a prescription.
     *
     * @param prescriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PrescriptionDTO> partialUpdate(PrescriptionDTO prescriptionDTO) {
        log.debug("Request to partially update Prescription : {}", prescriptionDTO);

        return prescriptionRepository
            .findById(prescriptionDTO.getId())
            .map(existingPrescription -> {
                prescriptionMapper.partialUpdate(existingPrescription, prescriptionDTO);

                return existingPrescription;
            })
            .map(prescriptionRepository::save)
            .map(prescriptionMapper::toDto);
    }

    /**
     * Get all the prescriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrescriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Prescriptions");
        return prescriptionRepository.findAll(pageable).map(prescriptionMapper::toDto);
    }

    /**
     * Get one prescription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrescriptionDTO> findOne(Long id) {
        log.debug("Request to get Prescription : {}", id);
        return prescriptionRepository
            .findById(id)
            .map(prescription -> {
                String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElse("anonymous");
                boolean isCreator = currentUserLogin.equals(prescription.getCreatedBy());
                boolean hasConsent = true;

                if (!isCreator && !currentUserLogin.equals("anonymous")) {
                    if (prescription.getEntry() != null) {
                        Boolean result = patientConsentClient.checkActiveConsent(
                            prescription.getEntry().getPatientPseudo(),
                            currentUserLogin
                        );
                        hasConsent = Boolean.TRUE.equals(result);
                    }
                }

                // Create Audit Log
                AuditLogClient.AuditLogDTO auditLog = new AuditLogClient.AuditLogDTO();
                auditLog.setTimestamp(Instant.now());
                auditLog.setPrincipal(currentUserLogin);
                auditLog.setResourceId("Prescription:" + id);
                auditLog.setAction("VIEW");
                auditLog.setIsSuccess(hasConsent);
                try {
                    auditLogClient.createAuditLog(auditLog);
                } catch (Exception e) {
                    log.error("Failed to send audit log", e);
                }

                if (!hasConsent && !isCreator) {
                    throw new SecurityException("Access denied: No active consent for this patient record.");
                }
                return prescription;
            })
            .map(prescriptionMapper::toDto);
    }

    /**
     * Delete the prescription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Prescription : {}", id);
        prescriptionRepository.deleteById(id);
    }
}
