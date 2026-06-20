package com.raizesdonordeste.app.domain.cardapio.model;

import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PratoTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPrato()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComUnidadeIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPrato()
                                .unidadeId(null)
                                .build())
                .withMessage("unidadeId não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComNomeBlank() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPrato()
                                .nome(null)
                                .build())
                .withMessage("nome não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPrato()
                                .nome("")
                                .build())
                .withMessage("nome não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComDescricaoBlank() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPrato()
                                .descricao(null)
                                .build())
                .withMessage("descricao não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPrato()
                                .descricao("")
                                .build())
                .withMessage("descricao não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComPrecoNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPrato()
                                .preco(null)
                                .build())
                .withMessage("preco não pode ser nulo.");
    }

    @Test
    void deveEditarPrato() {
        Prato prato = criarPrato().build();

        String novoNome = "Novo Nome";
        String novoDescricao = "Novo Descricao";
        Dinheiro novoPreco = new Dinheiro(2000L);

        assertThat(prato.getNome()).isNotEqualTo(novoNome);
        assertThat(prato.getDescricao()).isNotEqualTo(novoDescricao);
        assertThat(prato.getPreco()).isNotEqualTo(novoPreco);

        prato.editar(novoNome, novoDescricao, novoPreco);

        assertThat(prato.getNome()).isEqualTo(novoNome);
        assertThat(prato.getDescricao()).isEqualTo(novoDescricao);
        assertThat(prato.getPreco()).isEqualTo(novoPreco);
    }

    @Test
    void deveInativarPrato() {
        Prato prato = criarPrato().ativo(true).build();

        assertThat(prato.isAtivo()).isTrue();
        prato.inativar();
        assertThat(prato.isAtivo()).isFalse();
    }

    @Test
    void deveAlterarDisponibilidade() {
        Prato prato = criarPrato().disponivel(true).build();
        assertThat(prato.disponivelParaVenda()).isTrue();

        prato.alterarDisponibilidade(false);
        assertThat(prato.disponivelParaVenda()).isFalse();

        prato.alterarDisponibilidade(true);
        assertThat(prato.disponivelParaVenda()).isTrue();
    }

    private Prato.PratoBuilder criarPrato() {
        return Prato.builder()
                .id(Id.aleatorio())
                .unidadeId(Id.aleatorio())
                .nome("Nome")
                .descricao("Descricao")
                .preco(new Dinheiro(1000))
                .disponivel(true)
                .ativo(true);
    }
}
