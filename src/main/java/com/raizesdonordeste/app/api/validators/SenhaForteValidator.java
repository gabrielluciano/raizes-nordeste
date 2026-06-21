package com.raizesdonordeste.app.api.validators;

import com.raizesdonordeste.app.domain.identidade.services.SenhaValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SenhaForteValidator implements ConstraintValidator<SenhaForte, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        return SenhaValidator.validaSenha(value);
    }
}
