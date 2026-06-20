package com.raizesdonordeste.app.domain.comum.model;

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
}
