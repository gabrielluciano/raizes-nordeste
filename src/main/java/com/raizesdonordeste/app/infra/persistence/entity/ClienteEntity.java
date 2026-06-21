package com.raizesdonordeste.app.infra.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = ClienteEntity.TABLE_NAME, schema = ClienteEntity.SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteEntity {

    public static final String SCHEMA = "raizesnordeste";
    public static final String TABLE_NAME = "clientes";

    @Id
    private UUID id;
    private UUID contaId;
    private String nome;
    private String cpf;
    private String telefone;
    private String endereco;
    private LocalDate dataNascimento;
    private long saldoPontos;
    private boolean aceiteTermos;
    private LocalDateTime dataAceiteTermos;
    private String versaoTermos;
    private LocalDateTime dataCadastro;
}
