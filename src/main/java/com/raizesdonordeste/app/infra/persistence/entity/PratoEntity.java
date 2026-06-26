package com.raizesdonordeste.app.infra.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = PratoEntity.TABLE_NAME, schema = PratoEntity.SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PratoEntity {

    public static final String SCHEMA = "raizesnordeste";
    public static final String TABLE_NAME = "pratos";

    @Id
    private UUID id;
    private UUID unidadeId;
    private String nome;
    private String descricao;
    private long precoCentavos;
    private boolean disponivel;
    private boolean ativo;
}
