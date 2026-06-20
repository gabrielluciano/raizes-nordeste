package com.raizesdonordeste.app.domain.pedido.model;

import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ItemPedidoTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarItemPedido()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComPratoIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarItemPedido()
                                .pratoId(null)
                                .build())
                .withMessage("pratoId não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComQuantidadeIgualAZeroOuNegativa() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarItemPedido()
                                .quantidade(0)
                                .build())
                .withMessage("quantidade valor deve ser positivo.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarItemPedido()
                                .quantidade(-10)
                                .build())
                .withMessage("quantidade valor deve ser positivo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComPrecoUnitarioNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarItemPedido()
                                .precoUnitario(null)
                                .build())
                .withMessage("precoUnitario não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComPrecoUnitarioIgualAZeroOuNegativo() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarItemPedido()
                                .precoUnitario(new Dinheiro(0))
                                .build())
                .withMessage("precoUnitario valor deve ser positivo.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarItemPedido()
                                .precoUnitario(new Dinheiro(-10))
                                .build())
                .withMessage("precoUnitario valor deve ser positivo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComPratoNulo() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        ItemPedido.de(null, 1))
                .withMessage("prato não pode ser nulo.");
    }

    @Test
    void deveCalcularSubtotal() {
        ItemPedido itemPedido = criarItemPedido()
                .precoUnitario(new Dinheiro(50L))
                .quantidade(3)
                .build();

        assertThat(itemPedido.calcularSubtotal()).isEqualTo(new Dinheiro(150L));
    }

    private ItemPedido.ItemPedidoBuilder criarItemPedido() {
        return ItemPedido.builder()
                .id(Id.aleatorio())
                .pratoId(Id.aleatorio())
                .quantidade(1)
                .precoUnitario(new Dinheiro(1000));
    }
}
