package com.raizesdonordeste.app.domain.comum.model;

import java.util.List;
import java.util.function.Function;

public record Pagina<T>(
        List<T> items,
        int page,
        int size,
        long totalItems,
        int totalPages
) {
    public <O> Pagina<O> mapear(Function<T, O> mapper) {
        return new Pagina<>(
                items.stream().map(mapper).toList(),
                page,
                size,
                totalItems,
                totalPages
        );
    }
}
