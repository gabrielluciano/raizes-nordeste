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

    public static Id fromString(String id) {
        return new Id(id == null ? null : UUID.fromString(id));
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
