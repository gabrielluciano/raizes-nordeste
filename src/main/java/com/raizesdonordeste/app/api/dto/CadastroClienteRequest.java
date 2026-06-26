package com.raizesdonordeste.app.api.dto;

import com.raizesdonordeste.app.api.validators.SenhaForte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Getter
public class CadastroClienteRequest {

    @NotBlank(message = "nome não deve ser vazio.")
    private String nome;

    @CPF(message = "CPF inválido.")
    private String cpf;

    @NotBlank(message = "telefone não deve ser vazio.")
    private String telefone;

    @NotBlank
    private String endereco;

    @Email(message = "email inválido.")
    private String email;

    @SenhaForte
    private String senha;

    @NotNull(message = "dataNascimento não deve ser nula.")
    private LocalDate dataNascimento;

    @NotBlank(message = "versaoAceiteTermos não deve ser vazio.")
    private String versaoAceiteTermos;
}
