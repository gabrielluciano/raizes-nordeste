package com.raizesdonordeste.app.domain.identidade.repository;

import com.raizesdonordeste.app.domain.identidade.model.Base;

import java.util.Optional;

public interface BaseRepository {

    Optional<Base> obterPorSlug(String slug);
}
