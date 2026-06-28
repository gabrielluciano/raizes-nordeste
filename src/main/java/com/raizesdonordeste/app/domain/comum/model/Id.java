package com.raizesdonordeste.app.domain.comum.model;

import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;

import java.util.UUID;

public record Id(UUID id) {

    public Id {
        if (id == null) {
            throw new IllegalArgumentException("id não pode ser nulo.");
        }
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public static Id aleatorio() {
        return new Id(UUID.randomUUID());
    }

    public static Id fromString(String id) {
        if (id == null) {
            return new Id(null);
        }
        try {
            return new Id(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new ValidacaoException("Formato de id inválido: " + id);
        }
    }

    public static UUID toUUID(Id id) {
        if (id == null) {
            return null;
        }
        return id.id;
    }

    public static Id fromUUID(UUID id) {
        return id == null ? null : new Id(id);
    }
}
