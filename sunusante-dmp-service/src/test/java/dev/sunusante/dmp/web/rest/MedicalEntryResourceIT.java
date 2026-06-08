package dev.sunusante.dmp.web.rest;

import static dev.sunusante.dmp.domain.MedicalEntryAsserts.*;
import static dev.sunusante.dmp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sunusante.dmp.IntegrationTest;
import dev.sunusante.dmp.domain.MedicalEntry;
import dev.sunusante.dmp.domain.enumeration.MedicalEntryCategory;
import dev.sunusante.dmp.repository.MedicalEntryRepository;
import dev.sunusante.dmp.service.dto.MedicalEntryDTO;
import dev.sunusante.dmp.service.mapper.MedicalEntryMapper;
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
 * Integration tests for the {@link MedicalEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicalEntryResourceIT {

    private static final String DEFAULT_PATIENT_PSEUDO = "AAAAAAAAAA";
    private static final String UPDATED_PATIENT_PSEUDO = "BBBBBBBBBB";

    private static final MedicalEntryCategory DEFAULT_CATEGORY = MedicalEntryCategory.CONSULTATION;
    private static final MedicalEntryCategory UPDATED_CATEGORY = MedicalEntryCategory.IMAGING;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_CONFIDENTIALITY_LEVEL = 1;
    private static final Integer UPDATED_CONFIDENTIALITY_LEVEL = 2;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/medical-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicalEntryRepository medicalEntryRepository;

    @Autowired
    private MedicalEntryMapper medicalEntryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicalEntryMockMvc;

    private MedicalEntry medicalEntry;

    private MedicalEntry insertedMedicalEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalEntry createEntity(EntityManager em) {
        MedicalEntry medicalEntry = new MedicalEntry()
            .patientPseudo(DEFAULT_PATIENT_PSEUDO)
            .category(DEFAULT_CATEGORY)
            .content(DEFAULT_CONTENT)
            .confidentialityLevel(DEFAULT_CONFIDENTIALITY_LEVEL)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return medicalEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalEntry createUpdatedEntity(EntityManager em) {
        MedicalEntry medicalEntry = new MedicalEntry()
            .patientPseudo(UPDATED_PATIENT_PSEUDO)
            .category(UPDATED_CATEGORY)
            .content(UPDATED_CONTENT)
            .confidentialityLevel(UPDATED_CONFIDENTIALITY_LEVEL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return medicalEntry;
    }

    @BeforeEach
    public void initTest() {
        medicalEntry = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMedicalEntry != null) {
            medicalEntryRepository.delete(insertedMedicalEntry);
            insertedMedicalEntry = null;
        }
    }

    @Test
    @Transactional
    void createMedicalEntry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicalEntry
        MedicalEntryDTO medicalEntryDTO = medicalEntryMapper.toDto(medicalEntry);
        var returnedMedicalEntryDTO = om.readValue(
            restMedicalEntryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalEntryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicalEntryDTO.class
        );

        // Validate the MedicalEntry in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicalEntry = medicalEntryMapper.toEntity(returnedMedicalEntryDTO);
        assertMedicalEntryUpdatableFieldsEquals(returnedMedicalEntry, getPersistedMedicalEntry(returnedMedicalEntry));

        insertedMedicalEntry = returnedMedicalEntry;
    }

    @Test
    @Transactional
    void createMedicalEntryWithExistingId() throws Exception {
        // Create the MedicalEntry with an existing ID
        medicalEntry.setId(1L);
        MedicalEntryDTO medicalEntryDTO = medicalEntryMapper.toDto(medicalEntry);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPatientPseudoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalEntry.setPatientPseudo(null);

        // Create the MedicalEntry, which fails.
        MedicalEntryDTO medicalEntryDTO = medicalEntryMapper.toDto(medicalEntry);

        restMedicalEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalEntry.setCategory(null);

        // Create the MedicalEntry, which fails.
        MedicalEntryDTO medicalEntryDTO = medicalEntryMapper.toDto(medicalEntry);

        restMedicalEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicalEntries() throws Exception {
        // Initialize the database
        insertedMedicalEntry = medicalEntryRepository.saveAndFlush(medicalEntry);

        // Get all the medicalEntryList
        restMedicalEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].patientPseudo").value(hasItem(DEFAULT_PATIENT_PSEUDO)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].confidentialityLevel").value(hasItem(DEFAULT_CONFIDENTIALITY_LEVEL)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMedicalEntry() throws Exception {
        // Initialize the database
        insertedMedicalEntry = medicalEntryRepository.saveAndFlush(medicalEntry);

        // Get the medicalEntry
        restMedicalEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, medicalEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicalEntry.getId().intValue()))
            .andExpect(jsonPath("$.patientPseudo").value(DEFAULT_PATIENT_PSEUDO))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.confidentialityLevel").value(DEFAULT_CONFIDENTIALITY_LEVEL))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMedicalEntry() throws Exception {
        // Get the medicalEntry
        restMedicalEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicalEntry() throws Exception {
        // Initialize the database
        insertedMedicalEntry = medicalEntryRepository.saveAndFlush(medicalEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalEntry
        MedicalEntry updatedMedicalEntry = medicalEntryRepository.findById(medicalEntry.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicalEntry are not directly saved in db
        em.detach(updatedMedicalEntry);
        updatedMedicalEntry
            .patientPseudo(UPDATED_PATIENT_PSEUDO)
            .category(UPDATED_CATEGORY)
            .content(UPDATED_CONTENT)
            .confidentialityLevel(UPDATED_CONFIDENTIALITY_LEVEL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        MedicalEntryDTO medicalEntryDTO = medicalEntryMapper.toDto(updatedMedicalEntry);

        restMedicalEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicalEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicalEntryToMatchAllProperties(updatedMedicalEntry);
    }

    @Test
    @Transactional
    void putNonExistingMedicalEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalEntry.setId(longCount.incrementAndGet());

        // Create the MedicalEntry
        MedicalEntryDTO medicalEntryDTO = medicalEntryMapper.toDto(medicalEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicalEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalEntry.setId(longCount.incrementAndGet());

        // Create the MedicalEntry
        MedicalEntryDTO medicalEntryDTO = medicalEntryMapper.toDto(medicalEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicalEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalEntry.setId(longCount.incrementAndGet());

        // Create the MedicalEntry
        MedicalEntryDTO medicalEntryDTO = medicalEntryMapper.toDto(medicalEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalEntryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicalEntryWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalEntry = medicalEntryRepository.saveAndFlush(medicalEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalEntry using partial update
        MedicalEntry partialUpdatedMedicalEntry = new MedicalEntry();
        partialUpdatedMedicalEntry.setId(medicalEntry.getId());

        partialUpdatedMedicalEntry
            .patientPseudo(UPDATED_PATIENT_PSEUDO)
            .category(UPDATED_CATEGORY)
            .confidentialityLevel(UPDATED_CONFIDENTIALITY_LEVEL)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restMedicalEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalEntry))
            )
            .andExpect(status().isOk());

        // Validate the MedicalEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalEntryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicalEntry, medicalEntry),
            getPersistedMedicalEntry(medicalEntry)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicalEntryWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalEntry = medicalEntryRepository.saveAndFlush(medicalEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalEntry using partial update
        MedicalEntry partialUpdatedMedicalEntry = new MedicalEntry();
        partialUpdatedMedicalEntry.setId(medicalEntry.getId());

        partialUpdatedMedicalEntry
            .patientPseudo(UPDATED_PATIENT_PSEUDO)
            .category(UPDATED_CATEGORY)
            .content(UPDATED_CONTENT)
            .confidentialityLevel(UPDATED_CONFIDENTIALITY_LEVEL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restMedicalEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalEntry))
            )
            .andExpect(status().isOk());

        // Validate the MedicalEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalEntryUpdatableFieldsEquals(partialUpdatedMedicalEntry, getPersistedMedicalEntry(partialUpdatedMedicalEntry));
    }

    @Test
    @Transactional
    void patchNonExistingMedicalEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalEntry.setId(longCount.incrementAndGet());

        // Create the MedicalEntry
        MedicalEntryDTO medicalEntryDTO = medicalEntryMapper.toDto(medicalEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicalEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicalEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalEntry.setId(longCount.incrementAndGet());

        // Create the MedicalEntry
        MedicalEntryDTO medicalEntryDTO = medicalEntryMapper.toDto(medicalEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicalEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalEntry.setId(longCount.incrementAndGet());

        // Create the MedicalEntry
        MedicalEntryDTO medicalEntryDTO = medicalEntryMapper.toDto(medicalEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalEntryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicalEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicalEntry() throws Exception {
        // Initialize the database
        insertedMedicalEntry = medicalEntryRepository.saveAndFlush(medicalEntry);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicalEntry
        restMedicalEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicalEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicalEntryRepository.count();
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

    protected MedicalEntry getPersistedMedicalEntry(MedicalEntry medicalEntry) {
        return medicalEntryRepository.findById(medicalEntry.getId()).orElseThrow();
    }

    protected void assertPersistedMedicalEntryToMatchAllProperties(MedicalEntry expectedMedicalEntry) {
        assertMedicalEntryAllPropertiesEquals(expectedMedicalEntry, getPersistedMedicalEntry(expectedMedicalEntry));
    }

    protected void assertPersistedMedicalEntryToMatchUpdatableProperties(MedicalEntry expectedMedicalEntry) {
        assertMedicalEntryAllUpdatablePropertiesEquals(expectedMedicalEntry, getPersistedMedicalEntry(expectedMedicalEntry));
    }
}
