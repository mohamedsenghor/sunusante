package dev.sunusante.gateway.repository.rowmapper;

import dev.sunusante.gateway.domain.UserAccount;
import dev.sunusante.gateway.domain.enumeration.UserRole;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserAccount}, with proper type conversions.
 */
@Service
public class UserAccountRowMapper implements BiFunction<Row, String, UserAccount> {

    private final ColumnConverter converter;

    public UserAccountRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserAccount} stored in the database.
     */
    @Override
    public UserAccount apply(Row row, String prefix) {
        UserAccount entity = new UserAccount();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setMfaSecret(converter.fromRow(row, prefix + "_mfa_secret", String.class));
        entity.setRole(converter.fromRow(row, prefix + "_role", UserRole.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setInternalUserId(converter.fromRow(row, prefix + "_internal_user_id", Long.class));
        return entity;
    }
}
