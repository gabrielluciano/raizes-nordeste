package com.raizesdonordeste.app.domain.identidade.model;

import java.util.Set;

public enum Role {

    CLIENTE,
    OPERADOR,
    COZINHA,
    GERENTE,
    ADMINISTRADOR;

    public static final Set<Role> FUNCIONARIOS = Set.of(
            OPERADOR,
            COZINHA,
            GERENTE
    );

    public static boolean isCliente(Role role) {
        return CLIENTE.equals(role);
    }

    public static boolean isFuncionario(Role role) {
        return FUNCIONARIOS.contains(role);
    }
}
