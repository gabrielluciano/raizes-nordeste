package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Pagina;
import com.raizesdonordeste.app.domain.comum.model.Paginacao;
import com.raizesdonordeste.app.domain.pedido.model.CanalPedido;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import com.raizesdonordeste.app.infra.persistence.entity.ItemPedidoEntity;
import com.raizesdonordeste.app.infra.persistence.entity.PedidoEntity;
import com.raizesdonordeste.app.infra.persistence.jpa.ItemPedidoJpaRepository;
import com.raizesdonordeste.app.infra.persistence.jpa.PedidoJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.PedidoPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PedidoRepositoryImpl implements PedidoRepository {

    private final PedidoJpaRepository pedidoJpaRepository;
    private final ItemPedidoJpaRepository itemPedidoJpaRepository;
    private final PedidoPersistenceMapper mapper;

    @Override
    public void inserir(Pedido pedido) {
        PedidoEntity entity = mapper.toEntity(pedido);
        pedidoJpaRepository.save(entity);
        itemPedidoJpaRepository.saveAll(mapper.toItemEntities(pedido.getItensPedido(), entity.getId()));
    }

    @Override
    public void atualizar(Pedido pedido) {
        PedidoEntity entity = mapper.toEntity(pedido);
        pedidoJpaRepository.save(entity);
    }

    @Override
    public Optional<Pedido> obterPorId(Id id) {
        List<ItemPedidoEntity> itemPedidoEntities = itemPedidoJpaRepository.findAllByPedidoId(id.id());
        return pedidoJpaRepository.findById(id.id())
                .map(pedidoEntity -> mapper.toDomain(pedidoEntity, itemPedidoEntities));
    }

    @Override
    public Pagina<Pedido> listarTodos(Paginacao paginacao) {
        return toPagina(pedidoJpaRepository.findAll(toPageable(paginacao)));
    }

    @Override
    public Pagina<Pedido> listarPorCanal(CanalPedido canalPedido, Paginacao paginacao) {
        return toPagina(pedidoJpaRepository.findByCanal(canalPedido.name(), toPageable(paginacao)));
    }

    @Override
    public Pagina<Pedido> listarPorClienteId(Id clienteId, Paginacao paginacao) {
        return toPagina(pedidoJpaRepository.findByClienteId(clienteId.id(), toPageable(paginacao)));
    }

    @Override
    public Pagina<Pedido> listarPorClienteIdECanal(Id clienteId, CanalPedido canalPedido, Paginacao paginacao) {
        return toPagina(pedidoJpaRepository.findByClienteIdAndCanal(
                clienteId.id(), canalPedido.name(), toPageable(paginacao)));
    }

    @Override
    public Pagina<Pedido> listarPorUnidadeId(Id unidadeId, Paginacao paginacao) {
        return toPagina(pedidoJpaRepository.findByUnidadeId(unidadeId.id(), toPageable(paginacao)));
    }

    @Override
    public Pagina<Pedido> listarPorUnidadeIdECanal(Id unidadeId, CanalPedido canalPedido, Paginacao paginacao) {
        return toPagina(pedidoJpaRepository.findByUnidadeIdAndCanal(
                unidadeId.id(), canalPedido.name(), toPageable(paginacao)));
    }

    private Pageable toPageable(Paginacao paginacao) {
        return PageRequest.of(paginacao.page() - 1, paginacao.size());
    }

    private Map<UUID, List<ItemPedidoEntity>> carregarItensPorPedido(List<PedidoEntity> pedidos) {
        List<UUID> pedidoIds = pedidos.stream().map(PedidoEntity::getId).toList();
        if (pedidoIds.isEmpty()) {
            return Map.of();
        }
        return itemPedidoJpaRepository.findAllByPedidoIdIn(pedidoIds).stream()
                .collect(Collectors.groupingBy(ItemPedidoEntity::getPedidoId));
    }

    private Pagina<Pedido> toPagina(Page<PedidoEntity> page) {
        Map<UUID, List<ItemPedidoEntity>> itensPorPedido = carregarItensPorPedido(page.getContent());

        Page<Pedido> pedidos = page.map(pedidoEntity -> mapper.toDomain(
                pedidoEntity,
                itensPorPedido.getOrDefault(pedidoEntity.getId(), List.of())));

        return new Pagina<>(
                pedidos.getContent(),
                pedidos.getNumber() + 1,
                pedidos.getSize(),
                pedidos.getTotalElements(),
                pedidos.getTotalPages()
        );
    }
}
