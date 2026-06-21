package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Slug;
import com.raizesdonordeste.app.domain.identidade.model.Base;
import com.raizesdonordeste.app.domain.identidade.model.Role;
import com.raizesdonordeste.app.infra.persistence.entity.BaseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BasePersistenceMapper {

    public BaseEntity toEntity(Base base) {
        return new BaseEntity(
                base.getId().id(),
                base.getNome(),
                base.getSlug().valor(),
                roleToEntity(base.getRolesPermitidas())
        );
    }

    private String[] roleToEntity(Set<Role> roles) {
        return roles.stream()
                .map(Role::name)
                .toArray(String[]::new);
    }

    public Base toDomain(BaseEntity entity) {
        return Base.builder()
                .id(new Id(entity.getId()))
                .nome(entity.getNome())
                .slug(new Slug(entity.getSlug()))
                .rolesPermitidas(roleToDomain(entity.getRolesPermitidas()))
                .build();
    }

    private Set<Role> roleToDomain(String[] roles) {
        return Arrays.stream(roles)
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
}
