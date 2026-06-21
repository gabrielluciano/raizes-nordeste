package com.raizesdonordeste.app.infra.persistence.entity;

import com.raizesdonordeste.app.domain.identidade.model.Role;
import com.raizesdonordeste.app.domain.identidade.model.StatusConta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = ContaEntity.TABLE_NAME, schema = ContaEntity.SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaEntity {

    public static final String SCHEMA = "raizesnordeste";
    public static final String TABLE_NAME = "contas";

    @Id
    private UUID id;
    private UUID baseId;
    private String email;
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    private StatusConta status;

    @Enumerated(EnumType.STRING)
    private Role role;
}
