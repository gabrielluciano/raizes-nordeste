package com.raizesdonordeste.app.domain.identidade.model;

import java.time.LocalDate;

public record CadastrarClienteComando(
        String nome,
        String cpf,
        String telefone,
        String endereco,
        String email,
        String senha,
        LocalDate dataNascimento,
        String versaoAceiteTermos
) {
}
