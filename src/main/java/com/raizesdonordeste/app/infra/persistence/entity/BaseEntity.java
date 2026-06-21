package com.raizesdonordeste.app.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = BaseEntity.TABLE_NAME, schema = BaseEntity.SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    public static final String SCHEMA = "raizesnordeste";
    public static final String TABLE_NAME = "bases";

    @Id
    private UUID id;
    private String nome;
    private String slug;

    @Column(columnDefinition = "text[]")
    private String[] rolesPermitidas;
}
