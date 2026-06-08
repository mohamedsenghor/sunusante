package dev.sunusante.patient.web.rest;

import dev.sunusante.patient.repository.PatientConsentRepository;
import dev.sunusante.patient.service.PatientConsentService;
import dev.sunusante.patient.service.dto.PatientConsentDTO;
import dev.sunusante.patient.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link dev.sunusante.patient.domain.PatientConsent}.
 */
@RestController
@RequestMapping("/api/patient-consents")
public class PatientConsentResource {

    private static final Logger log = LoggerFactory.getLogger(PatientConsentResource.class);

    private static final String ENTITY_NAME = "sunusantePatientServicePatientConsent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientConsentService patientConsentService;

    private final PatientConsentRepository patientConsentRepository;

    public PatientConsentResource(PatientConsentService patientConsentService, PatientConsentRepository patientConsentRepository) {
        this.patientConsentService = patientConsentService;
        this.patientConsentRepository = patientConsentRepository;
    }

    /**
     * {@code POST  /patient-consents} : Create a new patientConsent.
     *
     * @param patientConsentDTO the patientConsentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patientConsentDTO, or with status {@code 400 (Bad Request)} if the patientConsent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PatientConsentDTO> createPatientConsent(@Valid @RequestBody PatientConsentDTO patientConsentDTO)
        throws URISyntaxException {
        log.debug("REST request to save PatientConsent : {}", patientConsentDTO);
        if (patientConsentDTO.getId() != null) {
            throw new BadRequestAlertException("A new patientConsent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        patientConsentDTO = patientConsentService.save(patientConsentDTO);
        return ResponseEntity.created(new URI("/api/patient-consents/" + patientConsentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, patientConsentDTO.getId().toString()))
            .body(patientConsentDTO);
    }

    /**
     * {@code PUT  /patient-consents/:id} : Updates an existing patientConsent.
     *
     * @param id the id of the patientConsentDTO to save.
     * @param patientConsentDTO the patientConsentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientConsentDTO,
     * or with status {@code 400 (Bad Request)} if the patientConsentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patientConsentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientConsentDTO> updatePatientConsent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PatientConsentDTO patientConsentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PatientConsent : {}, {}", id, patientConsentDTO);
        if (patientConsentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientConsentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientConsentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        patientConsentDTO = patientConsentService.update(patientConsentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patientConsentDTO.getId().toString()))
            .body(patientConsentDTO);
    }

    /**
     * {@code PATCH  /patient-consents/:id} : Partial updates given fields of an existing patientConsent, field will ignore if it is null
     *
     * @param id the id of the patientConsentDTO to save.
     * @param patientConsentDTO the patientConsentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientConsentDTO,
     * or with status {@code 400 (Bad Request)} if the patientConsentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the patientConsentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the patientConsentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PatientConsentDTO> partialUpdatePatientConsent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PatientConsentDTO patientConsentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PatientConsent partially : {}, {}", id, patientConsentDTO);
        if (patientConsentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientConsentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientConsentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PatientConsentDTO> result = patientConsentService.partialUpdate(patientConsentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patientConsentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /patient-consents} : get all the patientConsents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patientConsents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PatientConsentDTO>> getAllPatientConsents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PatientConsents");
        Page<PatientConsentDTO> page = patientConsentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patient-consents/:id} : get the "id" patientConsent.
     *
     * @param id the id of the patientConsentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientConsentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientConsentDTO> getPatientConsent(@PathVariable("id") Long id) {
        log.debug("REST request to get PatientConsent : {}", id);
        Optional<PatientConsentDTO> patientConsentDTO = patientConsentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patientConsentDTO);
    }

    /**
     * {@code DELETE  /patient-consents/:id} : delete the "id" patientConsent.
     *
     * @param id the id of the patientConsentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatientConsent(@PathVariable("id") Long id) {
        log.debug("REST request to delete PatientConsent : {}", id);
        patientConsentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /patient-consents/check} : check if a doctor has active consent for a patient.
     *
     * @param patientPseudo the pseudo of the patient.
     * @param doctorLogin the login of the doctor.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and body true if active consent exists, false otherwise.
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkActiveConsent(@RequestParam String patientPseudo, @RequestParam String doctorLogin) {
        log.debug("REST request to check active consent for patient {} and doctor {}", patientPseudo, doctorLogin);
        boolean hasConsent = patientConsentService.hasActiveConsent(patientPseudo, doctorLogin);
        return ResponseEntity.ok().body(hasConsent);
    }

    /**
     * {@code POST  /patient-consents/:id/approve} : approve a pending patientConsent.
     *
     * @param id the id of the patientConsentDTO to approve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientConsentDTO.
     */
    @PostMapping("/{id}/approve")
    public ResponseEntity<PatientConsentDTO> approvePatientConsent(@PathVariable("id") Long id) {
        log.debug("REST request to approve PatientConsent : {}", id);
        PatientConsentDTO patientConsentDTO = patientConsentService.approve(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(patientConsentDTO);
    }
}
