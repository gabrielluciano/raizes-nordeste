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
@Table(name = ItemPedidoEntity.TABLE_NAME, schema = ItemPedidoEntity.SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedidoEntity {

    public static final String SCHEMA = "raizesnordeste";
    public static final String TABLE_NAME = "itens_pedido";

    @Id
    private UUID id;
    private UUID pedidoId;
    private UUID pratoId;
    private int quantidade;
    private long precoUnitarioCentavos;
}
