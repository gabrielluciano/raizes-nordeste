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
@Table(name = PedidoEntity.TABLE_NAME, schema = PedidoEntity.SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoEntity {

    public static final String SCHEMA = "raizesnordeste";
    public static final String TABLE_NAME = "pedidos";

    @Id
    private UUID id;
    private UUID unidadeId;
    private UUID clienteId;
    private UUID funcionarioId;
    private String nomeCliente;
    private String canal;
    private String status;
    private boolean pickup;
    private LocalDateTime horarioPedido;
    private LocalDateTime horarioPreparo;
    private boolean consentimentoFidelizacao;
    private long valorTotalCentavos;
    private long valorDescontoPromocaoCentavos;
    private long valorDescontoPontosCentavos;
    private long valorFinalCentavos;
}
