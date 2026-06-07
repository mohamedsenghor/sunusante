package dev.sunusante.gateway.repository;

import dev.sunusante.gateway.domain.UserAccount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAccountRepository extends ReactiveCrudRepository<UserAccount, Long>, UserAccountRepositoryInternal {
    Flux<UserAccount> findAllBy(Pageable pageable);

    @Override
    Mono<UserAccount> findOneWithEagerRelationships(Long id);

    @Override
    Flux<UserAccount> findAllWithEagerRelationships();

    @Override
    Flux<UserAccount> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM user_account entity WHERE entity.internal_user_id = :id")
    Flux<UserAccount> findByInternalUser(Long id);

    @Query("SELECT * FROM user_account entity JOIN jhi_user u ON entity.internal_user_id = u.id WHERE u.login = :login")
    Mono<UserAccount> findByInternalUserLogin(String login);

    @Override
    <S extends UserAccount> Mono<S> save(S entity);

    @Override
    Flux<UserAccount> findAll();

    @Override
    Mono<UserAccount> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserAccountRepositoryInternal {
    <S extends UserAccount> Mono<S> save(S entity);

    Flux<UserAccount> findAllBy(Pageable pageable);

    Flux<UserAccount> findAll();

    Mono<UserAccount> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserAccount> findAllBy(Pageable pageable, Criteria criteria);

    Mono<UserAccount> findOneWithEagerRelationships(Long id);

    Flux<UserAccount> findAllWithEagerRelationships();

    Flux<UserAccount> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
