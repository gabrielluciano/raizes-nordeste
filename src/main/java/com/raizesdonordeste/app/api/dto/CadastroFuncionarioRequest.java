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

    @NotBlank(message = "nome não deve ser vazio.")
    private String nome;

    @NotNull(message = "unidadeId não deve ser nulo.")
    private UUID unidadeId;

    @NotBlank(message = "telefone não deve ser vazio.")
    private String telefone;

    @NotBlank(message = "endereco não deve ser vazio.")
    private String endereco;

    @Email(message = "email inválido.")
    private String email;

    @SenhaForte
    private String senha;

    @NotNull(message = "dataNascimento não deve ser nula.")
    private LocalDate dataNascimento;

    @NotNull(message = "role não deve ser nula.")
    private Role role;
}
