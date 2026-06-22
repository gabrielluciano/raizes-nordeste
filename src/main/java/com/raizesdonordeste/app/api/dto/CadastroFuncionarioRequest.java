package com.raizesdonordeste.app.api.dto;

import com.raizesdonordeste.app.api.validators.SenhaForte;
import com.raizesdonordeste.app.domain.identidade.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class CadastroFuncionarioRequest {

    @NotBlank
    private String nome;

    @NotNull
    private UUID unidadeId;

    @NotBlank
    private String telefone;

    @NotBlank
    private String endereco;

    @Email
    private String email;

    @SenhaForte
    private String senha;

    @NotNull
    private LocalDate dataNascimento;

    @NotNull
    private Role role;
}
