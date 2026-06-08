package dev.sunusante.patient.web.rest;

import static dev.sunusante.patient.domain.PatientConsentAsserts.*;
import static dev.sunusante.patient.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sunusante.patient.IntegrationTest;
import dev.sunusante.patient.domain.PatientConsent;
import dev.sunusante.patient.domain.enumeration.ConsentStatus;
import dev.sunusante.patient.repository.PatientConsentRepository;
import dev.sunusante.patient.service.dto.PatientConsentDTO;
import dev.sunusante.patient.service.mapper.PatientConsentMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PatientConsentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatientConsentResourceIT {

    private static final String DEFAULT_DOCTOR_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_DOCTOR_LOGIN = "BBBBBBBBBB";

    private static final Instant DEFAULT_CONSENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONSENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ConsentStatus DEFAULT_STATUS = ConsentStatus.ACTIVE;
    private static final ConsentStatus UPDATED_STATUS = ConsentStatus.EXPIRED;

    private static final String ENTITY_API_URL = "/api/patient-consents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PatientConsentRepository patientConsentRepository;

    @Autowired
    private PatientConsentMapper patientConsentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientConsentMockMvc;

    private PatientConsent patientConsent;

    private PatientConsent insertedPatientConsent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientConsent createEntity(EntityManager em) {
        PatientConsent patientConsent = new PatientConsent()
            .doctorLogin(DEFAULT_DOCTOR_LOGIN)
            .consentDate(DEFAULT_CONSENT_DATE)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .status(DEFAULT_STATUS);
        return patientConsent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientConsent createUpdatedEntity(EntityManager em) {
        PatientConsent patientConsent = new PatientConsent()
            .doctorLogin(UPDATED_DOCTOR_LOGIN)
            .consentDate(UPDATED_CONSENT_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .status(UPDATED_STATUS);
        return patientConsent;
    }

    @BeforeEach
    public void initTest() {
        patientConsent = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPatientConsent != null) {
            patientConsentRepository.delete(insertedPatientConsent);
            insertedPatientConsent = null;
        }
    }

    @Test
    @Transactional
    void createPatientConsent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PatientConsent
        PatientConsentDTO patientConsentDTO = patientConsentMapper.toDto(patientConsent);
        var returnedPatientConsentDTO = om.readValue(
            restPatientConsentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientConsentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PatientConsentDTO.class
        );

        // Validate the PatientConsent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPatientConsent = patientConsentMapper.toEntity(returnedPatientConsentDTO);
        assertPatientConsentUpdatableFieldsEquals(returnedPatientConsent, getPersistedPatientConsent(returnedPatientConsent));

        insertedPatientConsent = returnedPatientConsent;
    }

    @Test
    @Transactional
    void createPatientConsentWithExistingId() throws Exception {
        // Create the PatientConsent with an existing ID
        patientConsent.setId(1L);
        PatientConsentDTO patientConsentDTO = patientConsentMapper.toDto(patientConsent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientConsentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PatientConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDoctorLoginIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patientConsent.setDoctorLogin(null);

        // Create the PatientConsent, which fails.
        PatientConsentDTO patientConsentDTO = patientConsentMapper.toDto(patientConsent);

        restPatientConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientConsentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPatientConsents() throws Exception {
        // Initialize the database
        insertedPatientConsent = patientConsentRepository.saveAndFlush(patientConsent);

        // Get all the patientConsentList
        restPatientConsentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientConsent.getId().intValue())))
            .andExpect(jsonPath("$.[*].doctorLogin").value(hasItem(DEFAULT_DOCTOR_LOGIN)))
            .andExpect(jsonPath("$.[*].consentDate").value(hasItem(DEFAULT_CONSENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPatientConsent() throws Exception {
        // Initialize the database
        insertedPatientConsent = patientConsentRepository.saveAndFlush(patientConsent);

        // Get the patientConsent
        restPatientConsentMockMvc
            .perform(get(ENTITY_API_URL_ID, patientConsent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patientConsent.getId().intValue()))
            .andExpect(jsonPath("$.doctorLogin").value(DEFAULT_DOCTOR_LOGIN))
            .andExpect(jsonPath("$.consentDate").value(DEFAULT_CONSENT_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPatientConsent() throws Exception {
        // Get the patientConsent
        restPatientConsentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPatientConsent() throws Exception {
        // Initialize the database
        insertedPatientConsent = patientConsentRepository.saveAndFlush(patientConsent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patientConsent
        PatientConsent updatedPatientConsent = patientConsentRepository.findById(patientConsent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPatientConsent are not directly saved in db
        em.detach(updatedPatientConsent);
        updatedPatientConsent
            .doctorLogin(UPDATED_DOCTOR_LOGIN)
            .consentDate(UPDATED_CONSENT_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .status(UPDATED_STATUS);
        PatientConsentDTO patientConsentDTO = patientConsentMapper.toDto(updatedPatientConsent);

        restPatientConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientConsentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(patientConsentDTO))
            )
            .andExpect(status().isOk());

        // Validate the PatientConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPatientConsentToMatchAllProperties(updatedPatientConsent);
    }

    @Test
    @Transactional
    void putNonExistingPatientConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patientConsent.setId(longCount.incrementAndGet());

        // Create the PatientConsent
        PatientConsentDTO patientConsentDTO = patientConsentMapper.toDto(patientConsent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientConsentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(patientConsentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatientConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patientConsent.setId(longCount.incrementAndGet());

        // Create the PatientConsent
        PatientConsentDTO patientConsentDTO = patientConsentMapper.toDto(patientConsent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(patientConsentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatientConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patientConsent.setId(longCount.incrementAndGet());

        // Create the PatientConsent
        PatientConsentDTO patientConsentDTO = patientConsentMapper.toDto(patientConsent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientConsentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientConsentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientConsentWithPatch() throws Exception {
        // Initialize the database
        insertedPatientConsent = patientConsentRepository.saveAndFlush(patientConsent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patientConsent using partial update
        PatientConsent partialUpdatedPatientConsent = new PatientConsent();
        partialUpdatedPatientConsent.setId(patientConsent.getId());

        partialUpdatedPatientConsent.consentDate(UPDATED_CONSENT_DATE).expiryDate(UPDATED_EXPIRY_DATE);

        restPatientConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientConsent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPatientConsent))
            )
            .andExpect(status().isOk());

        // Validate the PatientConsent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPatientConsentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPatientConsent, patientConsent),
            getPersistedPatientConsent(patientConsent)
        );
    }

    @Test
    @Transactional
    void fullUpdatePatientConsentWithPatch() throws Exception {
        // Initialize the database
        insertedPatientConsent = patientConsentRepository.saveAndFlush(patientConsent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patientConsent using partial update
        PatientConsent partialUpdatedPatientConsent = new PatientConsent();
        partialUpdatedPatientConsent.setId(patientConsent.getId());

        partialUpdatedPatientConsent
            .doctorLogin(UPDATED_DOCTOR_LOGIN)
            .consentDate(UPDATED_CONSENT_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .status(UPDATED_STATUS);

        restPatientConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientConsent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPatientConsent))
            )
            .andExpect(status().isOk());

        // Validate the PatientConsent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPatientConsentUpdatableFieldsEquals(partialUpdatedPatientConsent, getPersistedPatientConsent(partialUpdatedPatientConsent));
    }

    @Test
    @Transactional
    void patchNonExistingPatientConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patientConsent.setId(longCount.incrementAndGet());

        // Create the PatientConsent
        PatientConsentDTO patientConsentDTO = patientConsentMapper.toDto(patientConsent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patientConsentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(patientConsentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatientConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patientConsent.setId(longCount.incrementAndGet());

        // Create the PatientConsent
        PatientConsentDTO patientConsentDTO = patientConsentMapper.toDto(patientConsent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(patientConsentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatientConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patientConsent.setId(longCount.incrementAndGet());

        // Create the PatientConsent
        PatientConsentDTO patientConsentDTO = patientConsentMapper.toDto(patientConsent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientConsentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(patientConsentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatientConsent() throws Exception {
        // Initialize the database
        insertedPatientConsent = patientConsentRepository.saveAndFlush(patientConsent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the patientConsent
        restPatientConsentMockMvc
            .perform(delete(ENTITY_API_URL_ID, patientConsent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return patientConsentRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PatientConsent getPersistedPatientConsent(PatientConsent patientConsent) {
        return patientConsentRepository.findById(patientConsent.getId()).orElseThrow();
    }

    protected void assertPersistedPatientConsentToMatchAllProperties(PatientConsent expectedPatientConsent) {
        assertPatientConsentAllPropertiesEquals(expectedPatientConsent, getPersistedPatientConsent(expectedPatientConsent));
    }

    protected void assertPersistedPatientConsentToMatchUpdatableProperties(PatientConsent expectedPatientConsent) {
        assertPatientConsentAllUpdatablePropertiesEquals(expectedPatientConsent, getPersistedPatientConsent(expectedPatientConsent));
    }
}
