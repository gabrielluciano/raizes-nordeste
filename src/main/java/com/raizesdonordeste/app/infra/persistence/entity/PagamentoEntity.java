package com.raizesdonordeste.app.infra.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = PagamentoEntity.TABLE_NAME, schema = PagamentoEntity.SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoEntity {

    public static final String SCHEMA = "raizesnordeste";
    public static final String TABLE_NAME = "pagamentos";

    @Id
    private UUID id;
    private UUID pedidoId;
    private String idempotencyKey;
    private String forma;
    private String status;
    private long valorCentavos;
    private String idTransacaoGateway;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataConfirmacao;
    private String motivoRecusa;
    private String qrCode;
    private LocalDateTime qrCodeValidoAte;
}
