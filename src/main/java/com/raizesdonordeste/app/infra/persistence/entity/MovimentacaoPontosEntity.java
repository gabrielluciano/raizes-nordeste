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
@Table(name = MovimentacaoPontosEntity.TABLE_NAME, schema = MovimentacaoPontosEntity.SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovimentacaoPontosEntity {

    public static final String SCHEMA = "raizesnordeste";
    public static final String TABLE_NAME = "movimentacoes_pontos";

    @Id
    private UUID id;
    private UUID clienteId;
    private UUID pedidoId;
    private String tipo;
    private long pontos;
    private LocalDateTime dataContabilizacao;
    private LocalDateTime dataExpiracao;
}
