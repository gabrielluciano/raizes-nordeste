package com.raizesdonordeste.app.domain.pedido.model;

import com.raizesdonordeste.app.domain.cardapio.model.Prato;
import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import lombok.Builder;

public record ItemPedido(Id id, Id pratoId, int quantidade, Dinheiro precoUnitario) {

    @Builder
    public ItemPedido(Id id, Id pratoId, int quantidade, Dinheiro precoUnitario) {
        this.id = Guarda.naoNulo(id, "id");
        this.pratoId = Guarda.naoNulo(pratoId, "pratoId");
        this.quantidade = Guarda.positivo(quantidade, "quantidade");
        this.precoUnitario = Guarda.positivo(precoUnitario, "precoUnitario");
    }

    public static ItemPedido de(Prato prato, int quantidade) {
        Guarda.naoNulo(prato, "prato");
        return new ItemPedido(Id.aleatorio(), prato.getId(), quantidade, prato.getPreco());
    }

    public Dinheiro calcularSubtotal() {
        return precoUnitario.multiplicar(quantidade);
    }
}
