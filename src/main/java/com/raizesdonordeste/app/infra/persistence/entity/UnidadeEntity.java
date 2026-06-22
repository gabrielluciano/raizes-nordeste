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
@Table(name = UnidadeEntity.TABLE_NAME, schema = UnidadeEntity.SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeEntity {

    public static final String SCHEMA = "raizesnordeste";
    public static final String TABLE_NAME = "unidades";

    @Id
    private UUID id;
    private String nome;
    private String endereco;
    private int horaDe;
    private int horaAte;
    private boolean ativa;
}
