package dev.sunusante.gateway.service.mapper;

import dev.sunusante.gateway.domain.User;
import dev.sunusante.gateway.domain.UserAccount;
import dev.sunusante.gateway.service.dto.UserAccountDTO;
import dev.sunusante.gateway.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAccount} and its DTO {@link UserAccountDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAccountMapper extends EntityMapper<UserAccountDTO, UserAccount> {
    @Mapping(target = "internalUser", source = "internalUser", qualifiedByName = "userLogin")
    UserAccountDTO toDto(UserAccount s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
