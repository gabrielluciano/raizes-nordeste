package com.raizesdonordeste.app.domain.cardapio.model;

import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import lombok.Builder;
import lombok.Getter;

public class Prato {

    @Getter
    private Id id;
    @Getter
    private Id unidadeId;
    @Getter
    private String nome;
    @Getter
    private String descricao;
    @Getter
    private Dinheiro preco;
    private boolean disponivel;
    @Getter
    private boolean ativo;

    @Builder
    public Prato(Id id, Id unidadeId, String nome, String descricao, Dinheiro preco, boolean disponivel, boolean ativo) {
        this.id = Guarda.naoNulo(id, "id");
        this.unidadeId = Guarda.naoNulo(unidadeId, "unidadeId");
        this.nome = Guarda.naoVazio(nome, "nome");
        this.descricao = Guarda.naoVazio(descricao, "descricao");
        this.preco = Guarda.naoNulo(preco, "preco");
        this.disponivel = disponivel;
        this.ativo = ativo;
    }

    public void editar(String nome, String descricao, Dinheiro preco) {
        this.nome = Guarda.naoVazio(nome, "nome");
        this.descricao = Guarda.naoVazio(descricao, "descricao");
        this.preco = Guarda.naoNulo(preco, "preco");
    }

    public void inativar() {
        this.ativo = false;
    }

    public boolean disponivelParaVenda() {
        return this.disponivel;
    }

    public void alterarDisponibilidade(boolean disponibilidade) {
        this.disponivel = disponibilidade;
    }

    public boolean pertenceAUnidade(Id unidadeId) {
        return this.unidadeId.equals(unidadeId);
    }
}
