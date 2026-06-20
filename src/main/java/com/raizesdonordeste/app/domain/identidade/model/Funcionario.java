package com.raizesdonordeste.app.domain.identidade.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Telefone;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import lombok.Builder;

import java.time.LocalDate;

public class Funcionario {

    private static final int IDADE_MINIMA_FUNCIONARIO = 16;

    private Id id;
    private Id contaId;
    private Id unidadeId;
    private String nome;
    private Telefone telefone;
    private String endereco;
    private LocalDate dataNascimento;

    @Builder
    public Funcionario(Id id, Id contaId, Id unidadeId, String nome, Telefone telefone, String endereco, LocalDate dataNascimento) {
        if (!dataNascimentoValida(dataNascimento)) {
            throw new IllegalArgumentException(
                    "data de nascimento inválida, deve possuir ao menos '%s' anos.".formatted(IDADE_MINIMA_FUNCIONARIO));
        }

        this.id = Guarda.naoNulo(id, "id");
        this.contaId = Guarda.naoNulo(contaId, "contaId");
        this.unidadeId = Guarda.naoNulo(unidadeId, "unidadeId");
        this.nome = Guarda.naoVazio(nome, "nome");
        this.telefone = Guarda.naoNulo(telefone, "telefone");
        this.endereco = Guarda.naoVazio(endereco, "endereco");
        this.dataNascimento = dataNascimento;
    }

    private boolean dataNascimentoValida(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            return false;
        }

        LocalDate dataLimite = LocalDate.now().minusYears(IDADE_MINIMA_FUNCIONARIO);

        return !dataNascimento.isAfter(dataLimite);
    }

    public boolean pertenceAUnidade(Id unidadeId) {
        if (unidadeId == null) {
            return false;
        }

        return this.unidadeId.equals(unidadeId);
    }
}
