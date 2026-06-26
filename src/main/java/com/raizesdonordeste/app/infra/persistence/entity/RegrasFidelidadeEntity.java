package com.raizesdonordeste.app.infra.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = RegrasFidelidadeEntity.TABLE_NAME, schema = RegrasFidelidadeEntity.SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegrasFidelidadeEntity {

    public static final String SCHEMA = "raizesnordeste";
    public static final String TABLE_NAME = "regras_fidelidade";

    @Id
    private UUID id;
    private BigDecimal valorPorPonto;
    private BigDecimal acumuloPorCentavo;
    private int validadePontosMeses;
    private int tetoResgatePercentual;
    private boolean ativa;
    private LocalDateTime ativadaEm;
    private LocalDateTime inativadaEm;
}
