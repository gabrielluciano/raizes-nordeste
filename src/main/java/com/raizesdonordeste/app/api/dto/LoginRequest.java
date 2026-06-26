package com.raizesdonordeste.app.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {

    @Email(message = "email inválido.")
    private String email;

    @NotBlank(message = "senha não deve ser vazia.")
    private String senha;
}
