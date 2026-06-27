package com.raizesdonordeste.app.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConfirmacaoPixRequest {

    @NotBlank
    private String transacaoId;

    @NotNull
    private LocalDateTime pagoEm;
}
