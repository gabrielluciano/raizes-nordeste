package com.raizesdonordeste.app.api.dto;

import com.raizesdonordeste.app.domain.pedido.model.CanalPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CriarPedidoRequest {

    private String unidadeId;

    @NotNull(message = "canal não deve ser nulo.")
    private CanalPedido canal;

    private String nomeCliente;

    @CPF(message = "CPF inválido.")
    private String cpfCliente;

    private boolean pickup;

    @NotNull(message = "horarioPedido não deve ser nulo.")
    private LocalDateTime horarioPedido;

    private LocalDateTime horarioPreparo;

    private boolean consentimentoFidelizacao;

    @PositiveOrZero(message = "pontosDesejados deve ser zero ou positivo.")
    private int pontosDesejados;

    @NotEmpty(message = "itens não deve ser vazio.")
    @Valid
    private List<ItemPedidoRequest> itens;

    @Getter
    public static class ItemPedidoRequest {

        @NotNull(message = "pratoId não deve ser nulo.")
        private String pratoId;

        @Positive(message = "quantidade deve ser positiva.")
        private int quantidade;
    }
}
