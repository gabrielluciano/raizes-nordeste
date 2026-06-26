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
@Table(name = PromocaoEntity.TABLE_NAME, schema = PromocaoEntity.SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromocaoEntity {

    public static final String SCHEMA = "raizesnordeste";
    public static final String TABLE_NAME = "promocoes";

    @Id
    private UUID id;
    private UUID pratoId;
    private String descricao;
    private double percentualDesconto;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private boolean ativa;
}
