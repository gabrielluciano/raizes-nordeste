package com.raizesdonordeste.app.infra.auth;

import com.raizesdonordeste.app.domain.identidade.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component("regrasAutorizacao")
public class RegrasAutorizacao {

    public boolean podeCriarFuncionario(Authentication authentication) {
        return temRole(authentication, Role.ADMINISTRADOR);
    }

    public boolean podeCriarPedido(Authentication authentication) {
        return temQualquerRole(authentication, Role.CLIENTE, Role.GERENTE, Role.OPERADOR, Role.ADMINISTRADOR);
    }

    public boolean podePagar(Authentication authentication) {
        return temQualquerRole(authentication, Role.CLIENTE, Role.GERENTE, Role.OPERADOR, Role.ADMINISTRADOR);
    }

    public boolean podeConsultarFidelidade(Authentication authentication) {
        return temRole(authentication, Role.CLIENTE);
    }

    public boolean podeAvancarStatusPedido(Authentication authentication) {
        return temQualquerRole(authentication, Role.ADMINISTRADOR, Role.GERENTE, Role.OPERADOR, Role.COZINHA);
    }

    private boolean temRole(Authentication authentication, Role role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> Objects.equals(authority.getAuthority(), "ROLE_" + role.name()));
    }

    private boolean temQualquerRole(Authentication authentication, Role... role) {
        return Arrays.stream(role)
                .anyMatch(candidato -> temRole(authentication, candidato));
    }
}
