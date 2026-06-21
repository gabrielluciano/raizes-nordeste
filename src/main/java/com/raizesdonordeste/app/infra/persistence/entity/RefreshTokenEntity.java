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
@Table(name = RefreshTokenEntity.TABLE_NAME, schema = RefreshTokenEntity.SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity {

    public static final String SCHEMA = "raizesnordeste";
    public static final String TABLE_NAME = "refresh_tokens";

    @Id
    private UUID id;
    private UUID contaId;
    private String tokenHash;
    private LocalDateTime expiraEm;
    private LocalDateTime revogadoEm;
}
