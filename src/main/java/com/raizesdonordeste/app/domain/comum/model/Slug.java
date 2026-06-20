package com.raizesdonordeste.app.domain.comum.model;

import java.util.regex.Pattern;

public record Slug(String valor) {

    public static final Pattern REGEX = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

    public Slug {
        if (!valido(valor)) {
            throw new IllegalArgumentException("slug inválido.");
        }
    }

    public static boolean valido(String slug) {
        if (slug == null) {
            return false;
        }

        return REGEX.matcher(slug).matches();
    }
}
