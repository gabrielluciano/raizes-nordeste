package com.raizesdonordeste.app.api.dto;

import com.raizesdonordeste.app.domain.pagamento.model.FormaPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PagamentoRequest {

    @NotBlank
    private String pedidoId;

    @NotNull
    private FormaPagamento formaPagamento;

    private String token;
}
