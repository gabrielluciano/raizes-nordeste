package com.raizesdonordeste.app.domain.identidade.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Slug;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import lombok.Builder;

import java.util.Set;

public class Base {

    private final Id id;
    private final String nome;
    private final Slug slug;
    private final Set<Role> rolesPermitidas;

    @Builder
    public Base(Id id, String nome, Slug slug, Set<Role> rolesPermitidas) {
        this.id = Guarda.naoNulo(id, "id");
        this.nome = Guarda.naoVazio(nome, "nome");
        this.slug = Guarda.naoNulo(slug, "slug");
        this.rolesPermitidas = Guarda.naoVazio(rolesPermitidas, "rolesPermitidas");
    }

    public boolean permiteRole(Role role) {
        if (role == null) {
            return false;
        }
        return rolesPermitidas.contains(role);
    }
}
