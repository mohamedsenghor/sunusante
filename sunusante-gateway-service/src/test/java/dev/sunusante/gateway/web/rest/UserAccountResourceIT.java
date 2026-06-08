package dev.sunusante.gateway.web.rest;

import static dev.sunusante.gateway.domain.UserAccountAsserts.*;
import static dev.sunusante.gateway.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sunusante.gateway.IntegrationTest;
import dev.sunusante.gateway.domain.UserAccount;
import dev.sunusante.gateway.domain.enumeration.UserRole;
import dev.sunusante.gateway.repository.EntityManager;
import dev.sunusante.gateway.repository.UserAccountRepository;
import dev.sunusante.gateway.repository.UserRepository;
import dev.sunusante.gateway.service.UserAccountService;
import dev.sunusante.gateway.service.dto.UserAccountDTO;
import dev.sunusante.gateway.service.mapper.UserAccountMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link UserAccountResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserAccountResourceIT {

    private static final String DEFAULT_MFA_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_MFA_SECRET = "BBBBBBBBBB";

    private static final UserRole DEFAULT_ROLE = UserRole.ROLE_PATIENT;
    private static final UserRole UPDATED_ROLE = UserRole.ROLE_DOCTOR;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserAccountRepository userAccountRepositoryMock;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Mock
    private UserAccountService userAccountServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserAccount userAccount;

    private UserAccount insertedUserAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccount createEntity(EntityManager em) {
        UserAccount userAccount = new UserAccount()
            .mfaSecret(DEFAULT_MFA_SECRET)
            .role(DEFAULT_ROLE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return userAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccount createUpdatedEntity(EntityManager em) {
        UserAccount userAccount = new UserAccount()
            .mfaSecret(UPDATED_MFA_SECRET)
            .role(UPDATED_ROLE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return userAccount;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserAccount.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        userAccount = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserAccount != null) {
            userAccountRepository.delete(insertedUserAccount).block();
            insertedUserAccount = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createUserAccount() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);
        var returnedUserAccountDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(UserAccountDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the UserAccount in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserAccount = userAccountMapper.toEntity(returnedUserAccountDTO);
        assertUserAccountUpdatableFieldsEquals(returnedUserAccount, getPersistedUserAccount(returnedUserAccount));

        insertedUserAccount = returnedUserAccount;
    }

    @Test
    void createUserAccountWithExistingId() throws Exception {
        // Create the UserAccount with an existing ID
        userAccount.setId(1L);
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllUserAccounts() {
        // Initialize the database
        insertedUserAccount = userAccountRepository.save(userAccount).block();

        // Get all the userAccountList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(userAccount.getId().intValue()))
            .jsonPath("$.[*].mfaSecret")
            .value(hasItem(DEFAULT_MFA_SECRET))
            .jsonPath("$.[*].role")
            .value(hasItem(DEFAULT_ROLE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserAccountsWithEagerRelationshipsIsEnabled() {
        when(userAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(userAccountServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserAccountsWithEagerRelationshipsIsNotEnabled() {
        when(userAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(userAccountRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getUserAccount() {
        // Initialize the database
        insertedUserAccount = userAccountRepository.save(userAccount).block();

        // Get the userAccount
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userAccount.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userAccount.getId().intValue()))
            .jsonPath("$.mfaSecret")
            .value(is(DEFAULT_MFA_SECRET))
            .jsonPath("$.role")
            .value(is(DEFAULT_ROLE.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.lastModifiedBy")
            .value(is(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    void getNonExistingUserAccount() {
        // Get the userAccount
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserAccount() throws Exception {
        // Initialize the database
        insertedUserAccount = userAccountRepository.save(userAccount).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAccount
        UserAccount updatedUserAccount = userAccountRepository.findById(userAccount.getId()).block();
        updatedUserAccount
            .mfaSecret(UPDATED_MFA_SECRET)
            .role(UPDATED_ROLE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(updatedUserAccount);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userAccountDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserAccountToMatchAllProperties(updatedUserAccount);
    }

    @Test
    void putNonExistingUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(longCount.incrementAndGet());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userAccountDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(longCount.incrementAndGet());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(longCount.incrementAndGet());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserAccountWithPatch() throws Exception {
        // Initialize the database
        insertedUserAccount = userAccountRepository.save(userAccount).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAccount using partial update
        UserAccount partialUpdatedUserAccount = new UserAccount();
        partialUpdatedUserAccount.setId(userAccount.getId());

        partialUpdatedUserAccount
            .mfaSecret(UPDATED_MFA_SECRET)
            .role(UPDATED_ROLE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserAccount.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserAccount))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAccountUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserAccount, userAccount),
            getPersistedUserAccount(userAccount)
        );
    }

    @Test
    void fullUpdateUserAccountWithPatch() throws Exception {
        // Initialize the database
        insertedUserAccount = userAccountRepository.save(userAccount).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAccount using partial update
        UserAccount partialUpdatedUserAccount = new UserAccount();
        partialUpdatedUserAccount.setId(userAccount.getId());

        partialUpdatedUserAccount
            .mfaSecret(UPDATED_MFA_SECRET)
            .role(UPDATED_ROLE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserAccount.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserAccount))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAccountUpdatableFieldsEquals(partialUpdatedUserAccount, getPersistedUserAccount(partialUpdatedUserAccount));
    }

    @Test
    void patchNonExistingUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(longCount.incrementAndGet());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userAccountDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(longCount.incrementAndGet());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(longCount.incrementAndGet());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userAccountDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserAccount() {
        // Initialize the database
        insertedUserAccount = userAccountRepository.save(userAccount).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userAccount
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userAccount.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userAccountRepository.count().block();
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

    protected UserAccount getPersistedUserAccount(UserAccount userAccount) {
        return userAccountRepository.findById(userAccount.getId()).block();
    }

    protected void assertPersistedUserAccountToMatchAllProperties(UserAccount expectedUserAccount) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserAccountAllPropertiesEquals(expectedUserAccount, getPersistedUserAccount(expectedUserAccount));
        assertUserAccountUpdatableFieldsEquals(expectedUserAccount, getPersistedUserAccount(expectedUserAccount));
    }

    protected void assertPersistedUserAccountToMatchUpdatableProperties(UserAccount expectedUserAccount) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserAccountAllUpdatablePropertiesEquals(expectedUserAccount, getPersistedUserAccount(expectedUserAccount));
        assertUserAccountUpdatableFieldsEquals(expectedUserAccount, getPersistedUserAccount(expectedUserAccount));
    }
}
