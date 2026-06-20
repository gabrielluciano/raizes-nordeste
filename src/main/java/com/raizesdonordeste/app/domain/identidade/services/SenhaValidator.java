package com.raizesdonordeste.app.domain.identidade.services;

import java.util.regex.Pattern;

public class SenhaValidator {

    public static final Pattern REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])\\S{8,}$");

    private SenhaValidator() {
    }

    public static boolean validaSenha(String senha) {
        if (senha == null) {
            return false;
        }

        return REGEX.matcher(senha).matches();
    }
}
