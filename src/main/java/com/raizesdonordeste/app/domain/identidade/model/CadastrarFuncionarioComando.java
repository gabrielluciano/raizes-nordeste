package com.raizesdonordeste.app.domain.identidade.model;

import com.raizesdonordeste.app.domain.comum.model.Id;

import java.time.LocalDate;

public record CadastrarFuncionarioComando(
        String nome,
        Id unidadeId,
        String telefone,
        String endereco,
        String email,
        String senha,
        LocalDate dataNascimento,
        Role role
) {
}
