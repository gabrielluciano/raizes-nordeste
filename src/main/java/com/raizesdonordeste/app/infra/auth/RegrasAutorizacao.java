package com.raizesdonordeste.app.infra.auth;

import com.raizesdonordeste.app.domain.identidade.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("regrasAutorizacao")
public class RegrasAutorizacao {

    public boolean podeCriarFuncionario(Authentication authentication) {
        return temRole(authentication, Role.ADMINISTRADOR);
    }

    private boolean temRole(Authentication authentication, Role role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> Objects.equals(authority.getAuthority(), "ROLE_" + role.name()));
    }
}
