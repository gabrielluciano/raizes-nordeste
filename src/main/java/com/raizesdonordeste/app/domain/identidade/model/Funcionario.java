package com.raizesdonordeste.app.domain.identidade.model;

import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Telefone;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Funcionario {

    private static final int IDADE_MINIMA_FUNCIONARIO = 16;

    private final Id id;
    private final Id contaId;
    private final Id unidadeId;
    private final String nome;
    private final Telefone telefone;
    private final String endereco;
    private final LocalDate dataNascimento;

    @Builder
    public Funcionario(Id id, Id contaId, Id unidadeId, String nome, Telefone telefone, String endereco, LocalDate dataNascimento) {
        if (!dataNascimentoValida(dataNascimento)) {
            throw new ValidacaoException(
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

    public static Funcionario criar(
            Id contaId,
            Id unidadeId,
            String nome,
            String telefone,
            String endereco,
            LocalDate dataNascimento
    ) {
        return new Funcionario(
                Id.aleatorio(),
                contaId,
                unidadeId,
                nome,
                new Telefone(telefone),
                endereco,
                dataNascimento
        );
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
