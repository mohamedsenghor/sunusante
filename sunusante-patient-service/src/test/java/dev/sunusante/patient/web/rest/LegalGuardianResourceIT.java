package dev.sunusante.patient.web.rest;

import static dev.sunusante.patient.domain.LegalGuardianAsserts.*;
import static dev.sunusante.patient.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sunusante.patient.IntegrationTest;
import dev.sunusante.patient.domain.LegalGuardian;
import dev.sunusante.patient.repository.LegalGuardianRepository;
import dev.sunusante.patient.service.dto.LegalGuardianDTO;
import dev.sunusante.patient.service.mapper.LegalGuardianMapper;
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
 * Integration tests for the {@link LegalGuardianResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LegalGuardianResourceIT {

    private static final String DEFAULT_GUARDIAN_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/legal-guardians";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LegalGuardianRepository legalGuardianRepository;

    @Autowired
    private LegalGuardianMapper legalGuardianMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLegalGuardianMockMvc;

    private LegalGuardian legalGuardian;

    private LegalGuardian insertedLegalGuardian;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LegalGuardian createEntity(EntityManager em) {
        LegalGuardian legalGuardian = new LegalGuardian()
            .guardianType(DEFAULT_GUARDIAN_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return legalGuardian;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LegalGuardian createUpdatedEntity(EntityManager em) {
        LegalGuardian legalGuardian = new LegalGuardian()
            .guardianType(UPDATED_GUARDIAN_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return legalGuardian;
    }

    @BeforeEach
    public void initTest() {
        legalGuardian = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLegalGuardian != null) {
            legalGuardianRepository.delete(insertedLegalGuardian);
            insertedLegalGuardian = null;
        }
    }

    @Test
    @Transactional
    void createLegalGuardian() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LegalGuardian
        LegalGuardianDTO legalGuardianDTO = legalGuardianMapper.toDto(legalGuardian);
        var returnedLegalGuardianDTO = om.readValue(
            restLegalGuardianMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(legalGuardianDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LegalGuardianDTO.class
        );

        // Validate the LegalGuardian in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLegalGuardian = legalGuardianMapper.toEntity(returnedLegalGuardianDTO);
        assertLegalGuardianUpdatableFieldsEquals(returnedLegalGuardian, getPersistedLegalGuardian(returnedLegalGuardian));

        insertedLegalGuardian = returnedLegalGuardian;
    }

    @Test
    @Transactional
    void createLegalGuardianWithExistingId() throws Exception {
        // Create the LegalGuardian with an existing ID
        legalGuardian.setId(1L);
        LegalGuardianDTO legalGuardianDTO = legalGuardianMapper.toDto(legalGuardian);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLegalGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(legalGuardianDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LegalGuardian in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLegalGuardians() throws Exception {
        // Initialize the database
        insertedLegalGuardian = legalGuardianRepository.saveAndFlush(legalGuardian);

        // Get all the legalGuardianList
        restLegalGuardianMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(legalGuardian.getId().intValue())))
            .andExpect(jsonPath("$.[*].guardianType").value(hasItem(DEFAULT_GUARDIAN_TYPE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getLegalGuardian() throws Exception {
        // Initialize the database
        insertedLegalGuardian = legalGuardianRepository.saveAndFlush(legalGuardian);

        // Get the legalGuardian
        restLegalGuardianMockMvc
            .perform(get(ENTITY_API_URL_ID, legalGuardian.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(legalGuardian.getId().intValue()))
            .andExpect(jsonPath("$.guardianType").value(DEFAULT_GUARDIAN_TYPE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLegalGuardian() throws Exception {
        // Get the legalGuardian
        restLegalGuardianMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLegalGuardian() throws Exception {
        // Initialize the database
        insertedLegalGuardian = legalGuardianRepository.saveAndFlush(legalGuardian);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the legalGuardian
        LegalGuardian updatedLegalGuardian = legalGuardianRepository.findById(legalGuardian.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLegalGuardian are not directly saved in db
        em.detach(updatedLegalGuardian);
        updatedLegalGuardian
            .guardianType(UPDATED_GUARDIAN_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        LegalGuardianDTO legalGuardianDTO = legalGuardianMapper.toDto(updatedLegalGuardian);

        restLegalGuardianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, legalGuardianDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(legalGuardianDTO))
            )
            .andExpect(status().isOk());

        // Validate the LegalGuardian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLegalGuardianToMatchAllProperties(updatedLegalGuardian);
    }

    @Test
    @Transactional
    void putNonExistingLegalGuardian() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        legalGuardian.setId(longCount.incrementAndGet());

        // Create the LegalGuardian
        LegalGuardianDTO legalGuardianDTO = legalGuardianMapper.toDto(legalGuardian);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLegalGuardianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, legalGuardianDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(legalGuardianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LegalGuardian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLegalGuardian() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        legalGuardian.setId(longCount.incrementAndGet());

        // Create the LegalGuardian
        LegalGuardianDTO legalGuardianDTO = legalGuardianMapper.toDto(legalGuardian);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLegalGuardianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(legalGuardianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LegalGuardian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLegalGuardian() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        legalGuardian.setId(longCount.incrementAndGet());

        // Create the LegalGuardian
        LegalGuardianDTO legalGuardianDTO = legalGuardianMapper.toDto(legalGuardian);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLegalGuardianMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(legalGuardianDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LegalGuardian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLegalGuardianWithPatch() throws Exception {
        // Initialize the database
        insertedLegalGuardian = legalGuardianRepository.saveAndFlush(legalGuardian);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the legalGuardian using partial update
        LegalGuardian partialUpdatedLegalGuardian = new LegalGuardian();
        partialUpdatedLegalGuardian.setId(legalGuardian.getId());

        partialUpdatedLegalGuardian.guardianType(UPDATED_GUARDIAN_TYPE).createdBy(UPDATED_CREATED_BY).createdDate(UPDATED_CREATED_DATE);

        restLegalGuardianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLegalGuardian.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLegalGuardian))
            )
            .andExpect(status().isOk());

        // Validate the LegalGuardian in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLegalGuardianUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLegalGuardian, legalGuardian),
            getPersistedLegalGuardian(legalGuardian)
        );
    }

    @Test
    @Transactional
    void fullUpdateLegalGuardianWithPatch() throws Exception {
        // Initialize the database
        insertedLegalGuardian = legalGuardianRepository.saveAndFlush(legalGuardian);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the legalGuardian using partial update
        LegalGuardian partialUpdatedLegalGuardian = new LegalGuardian();
        partialUpdatedLegalGuardian.setId(legalGuardian.getId());

        partialUpdatedLegalGuardian
            .guardianType(UPDATED_GUARDIAN_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restLegalGuardianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLegalGuardian.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLegalGuardian))
            )
            .andExpect(status().isOk());

        // Validate the LegalGuardian in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLegalGuardianUpdatableFieldsEquals(partialUpdatedLegalGuardian, getPersistedLegalGuardian(partialUpdatedLegalGuardian));
    }

    @Test
    @Transactional
    void patchNonExistingLegalGuardian() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        legalGuardian.setId(longCount.incrementAndGet());

        // Create the LegalGuardian
        LegalGuardianDTO legalGuardianDTO = legalGuardianMapper.toDto(legalGuardian);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLegalGuardianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, legalGuardianDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(legalGuardianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LegalGuardian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLegalGuardian() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        legalGuardian.setId(longCount.incrementAndGet());

        // Create the LegalGuardian
        LegalGuardianDTO legalGuardianDTO = legalGuardianMapper.toDto(legalGuardian);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLegalGuardianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(legalGuardianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LegalGuardian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLegalGuardian() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        legalGuardian.setId(longCount.incrementAndGet());

        // Create the LegalGuardian
        LegalGuardianDTO legalGuardianDTO = legalGuardianMapper.toDto(legalGuardian);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLegalGuardianMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(legalGuardianDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LegalGuardian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLegalGuardian() throws Exception {
        // Initialize the database
        insertedLegalGuardian = legalGuardianRepository.saveAndFlush(legalGuardian);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the legalGuardian
        restLegalGuardianMockMvc
            .perform(delete(ENTITY_API_URL_ID, legalGuardian.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return legalGuardianRepository.count();
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

    protected LegalGuardian getPersistedLegalGuardian(LegalGuardian legalGuardian) {
        return legalGuardianRepository.findById(legalGuardian.getId()).orElseThrow();
    }

    protected void assertPersistedLegalGuardianToMatchAllProperties(LegalGuardian expectedLegalGuardian) {
        assertLegalGuardianAllPropertiesEquals(expectedLegalGuardian, getPersistedLegalGuardian(expectedLegalGuardian));
    }

    protected void assertPersistedLegalGuardianToMatchUpdatableProperties(LegalGuardian expectedLegalGuardian) {
        assertLegalGuardianAllUpdatablePropertiesEquals(expectedLegalGuardian, getPersistedLegalGuardian(expectedLegalGuardian));
    }
}
