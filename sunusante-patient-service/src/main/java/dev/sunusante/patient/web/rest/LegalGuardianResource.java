package dev.sunusante.patient.web.rest;

import dev.sunusante.patient.repository.LegalGuardianRepository;
import dev.sunusante.patient.service.LegalGuardianService;
import dev.sunusante.patient.service.dto.LegalGuardianDTO;
import dev.sunusante.patient.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link dev.sunusante.patient.domain.LegalGuardian}.
 */
@RestController
@RequestMapping("/api/legal-guardians")
public class LegalGuardianResource {

    private static final Logger log = LoggerFactory.getLogger(LegalGuardianResource.class);

    private static final String ENTITY_NAME = "sunusantePatientServiceLegalGuardian";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LegalGuardianService legalGuardianService;

    private final LegalGuardianRepository legalGuardianRepository;

    public LegalGuardianResource(LegalGuardianService legalGuardianService, LegalGuardianRepository legalGuardianRepository) {
        this.legalGuardianService = legalGuardianService;
        this.legalGuardianRepository = legalGuardianRepository;
    }

    /**
     * {@code POST  /legal-guardians} : Create a new legalGuardian.
     *
     * @param legalGuardianDTO the legalGuardianDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new legalGuardianDTO, or with status {@code 400 (Bad Request)} if the legalGuardian has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LegalGuardianDTO> createLegalGuardian(@RequestBody LegalGuardianDTO legalGuardianDTO) throws URISyntaxException {
        log.debug("REST request to save LegalGuardian : {}", legalGuardianDTO);
        if (legalGuardianDTO.getId() != null) {
            throw new BadRequestAlertException("A new legalGuardian cannot already have an ID", ENTITY_NAME, "idexists");
        }
        legalGuardianDTO = legalGuardianService.save(legalGuardianDTO);
        return ResponseEntity.created(new URI("/api/legal-guardians/" + legalGuardianDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, legalGuardianDTO.getId().toString()))
            .body(legalGuardianDTO);
    }

    /**
     * {@code PUT  /legal-guardians/:id} : Updates an existing legalGuardian.
     *
     * @param id the id of the legalGuardianDTO to save.
     * @param legalGuardianDTO the legalGuardianDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated legalGuardianDTO,
     * or with status {@code 400 (Bad Request)} if the legalGuardianDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the legalGuardianDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LegalGuardianDTO> updateLegalGuardian(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LegalGuardianDTO legalGuardianDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LegalGuardian : {}, {}", id, legalGuardianDTO);
        if (legalGuardianDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, legalGuardianDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!legalGuardianRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        legalGuardianDTO = legalGuardianService.update(legalGuardianDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, legalGuardianDTO.getId().toString()))
            .body(legalGuardianDTO);
    }

    /**
     * {@code PATCH  /legal-guardians/:id} : Partial updates given fields of an existing legalGuardian, field will ignore if it is null
     *
     * @param id the id of the legalGuardianDTO to save.
     * @param legalGuardianDTO the legalGuardianDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated legalGuardianDTO,
     * or with status {@code 400 (Bad Request)} if the legalGuardianDTO is not valid,
     * or with status {@code 404 (Not Found)} if the legalGuardianDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the legalGuardianDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LegalGuardianDTO> partialUpdateLegalGuardian(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LegalGuardianDTO legalGuardianDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LegalGuardian partially : {}, {}", id, legalGuardianDTO);
        if (legalGuardianDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, legalGuardianDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!legalGuardianRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LegalGuardianDTO> result = legalGuardianService.partialUpdate(legalGuardianDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, legalGuardianDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /legal-guardians} : get all the legalGuardians.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of legalGuardians in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LegalGuardianDTO>> getAllLegalGuardians(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LegalGuardians");
        Page<LegalGuardianDTO> page = legalGuardianService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /legal-guardians/:id} : get the "id" legalGuardian.
     *
     * @param id the id of the legalGuardianDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the legalGuardianDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LegalGuardianDTO> getLegalGuardian(@PathVariable("id") Long id) {
        log.debug("REST request to get LegalGuardian : {}", id);
        Optional<LegalGuardianDTO> legalGuardianDTO = legalGuardianService.findOne(id);
        return ResponseUtil.wrapOrNotFound(legalGuardianDTO);
    }

    /**
     * {@code DELETE  /legal-guardians/:id} : delete the "id" legalGuardian.
     *
     * @param id the id of the legalGuardianDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLegalGuardian(@PathVariable("id") Long id) {
        log.debug("REST request to delete LegalGuardian : {}", id);
        legalGuardianService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
