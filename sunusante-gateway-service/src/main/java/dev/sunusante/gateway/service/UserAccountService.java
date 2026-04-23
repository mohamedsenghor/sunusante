package dev.sunusante.gateway.service;

import dev.sunusante.gateway.repository.UserAccountRepository;
import dev.sunusante.gateway.service.dto.UserAccountDTO;
import dev.sunusante.gateway.service.mapper.UserAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link dev.sunusante.gateway.domain.UserAccount}.
 */
@Service
@Transactional
public class UserAccountService {

    private static final Logger log = LoggerFactory.getLogger(UserAccountService.class);

    private final UserAccountRepository userAccountRepository;

    private final UserAccountMapper userAccountMapper;

    public UserAccountService(UserAccountRepository userAccountRepository, UserAccountMapper userAccountMapper) {
        this.userAccountRepository = userAccountRepository;
        this.userAccountMapper = userAccountMapper;
    }

    /**
     * Save a userAccount.
     *
     * @param userAccountDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserAccountDTO> save(UserAccountDTO userAccountDTO) {
        log.debug("Request to save UserAccount : {}", userAccountDTO);
        return userAccountRepository.save(userAccountMapper.toEntity(userAccountDTO)).map(userAccountMapper::toDto);
    }

    /**
     * Update a userAccount.
     *
     * @param userAccountDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserAccountDTO> update(UserAccountDTO userAccountDTO) {
        log.debug("Request to update UserAccount : {}", userAccountDTO);
        return userAccountRepository.save(userAccountMapper.toEntity(userAccountDTO)).map(userAccountMapper::toDto);
    }

    /**
     * Partially update a userAccount.
     *
     * @param userAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UserAccountDTO> partialUpdate(UserAccountDTO userAccountDTO) {
        log.debug("Request to partially update UserAccount : {}", userAccountDTO);

        return userAccountRepository
            .findById(userAccountDTO.getId())
            .map(existingUserAccount -> {
                userAccountMapper.partialUpdate(existingUserAccount, userAccountDTO);

                return existingUserAccount;
            })
            .flatMap(userAccountRepository::save)
            .map(userAccountMapper::toDto);
    }

    /**
     * Get all the userAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UserAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserAccounts");
        return userAccountRepository.findAllBy(pageable).map(userAccountMapper::toDto);
    }

    /**
     * Get all the userAccounts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<UserAccountDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userAccountRepository.findAllWithEagerRelationships(pageable).map(userAccountMapper::toDto);
    }

    /**
     * Returns the number of userAccounts available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return userAccountRepository.count();
    }

    /**
     * Get one userAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UserAccountDTO> findOne(Long id) {
        log.debug("Request to get UserAccount : {}", id);
        return userAccountRepository.findOneWithEagerRelationships(id).map(userAccountMapper::toDto);
    }

    /**
     * Delete the userAccount by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserAccount : {}", id);
        return userAccountRepository.deleteById(id);
    }
}
