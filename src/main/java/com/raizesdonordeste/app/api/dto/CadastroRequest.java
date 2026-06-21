package com.raizesdonordeste.app.api.dto;

import com.raizesdonordeste.app.api.validators.SenhaForte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Getter
public class CadastroRequest {

    @NotBlank
    private String nome;

    @CPF
    private String cpf;

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

    @NotBlank
    private String versaoAceiteTermos;
}
