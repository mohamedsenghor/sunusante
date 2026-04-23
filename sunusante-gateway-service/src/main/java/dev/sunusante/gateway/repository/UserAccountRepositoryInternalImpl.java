package dev.sunusante.gateway.repository;

import dev.sunusante.gateway.domain.UserAccount;
import dev.sunusante.gateway.repository.rowmapper.UserAccountRowMapper;
import dev.sunusante.gateway.repository.rowmapper.UserRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the UserAccount entity.
 */
@SuppressWarnings("unused")
class UserAccountRepositoryInternalImpl extends SimpleR2dbcRepository<UserAccount, Long> implements UserAccountRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final UserAccountRowMapper useraccountMapper;

    private static final Table entityTable = Table.aliased("user_account", EntityManager.ENTITY_ALIAS);
    private static final Table internalUserTable = Table.aliased("jhi_user", "internalUser");

    public UserAccountRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        UserAccountRowMapper useraccountMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserAccount.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.useraccountMapper = useraccountMapper;
    }

    @Override
    public Flux<UserAccount> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserAccount> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserAccountSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(internalUserTable, "internalUser"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(internalUserTable)
            .on(Column.create("internal_user_id", entityTable))
            .equals(Column.create("id", internalUserTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserAccount.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserAccount> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserAccount> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<UserAccount> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<UserAccount> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<UserAccount> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private UserAccount process(Row row, RowMetadata metadata) {
        UserAccount entity = useraccountMapper.apply(row, "e");
        entity.setInternalUser(userMapper.apply(row, "internalUser"));
        return entity;
    }

    @Override
    public <S extends UserAccount> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
