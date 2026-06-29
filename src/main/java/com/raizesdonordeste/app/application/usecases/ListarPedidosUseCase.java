package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Pagina;
import com.raizesdonordeste.app.domain.comum.model.Paginacao;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.domain.identidade.model.Funcionario;
import com.raizesdonordeste.app.domain.identidade.model.Role;
import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import com.raizesdonordeste.app.domain.identidade.repository.FuncionarioRepository;
import com.raizesdonordeste.app.domain.pedido.model.CanalPedido;
import com.raizesdonordeste.app.domain.pedido.model.ListarPedidosComando;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarPedidosUseCase implements CasoDeUso<ListarPedidosComando, Pagina<Pedido>> {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;

    @Override
    public Pagina<Pedido> executar(ListarPedidosComando comando) {
        Role role = comando.role();

        if (Role.ADMINISTRADOR.equals(role)) {
            return listarTodos(comando);
        }

        if (Role.isCliente(role)) {
            return clienteRepository.obterPorContaId(comando.contaId())
                    .map(Cliente::getId)
                    .map(clienteId -> listarPorCliente(clienteId, comando))
                    .orElseGet(() -> paginaVazia(comando));
        }

        if (Role.isFuncionario(role)) {
            return funcionarioRepository.obterPorContaId(comando.contaId())
                    .map(Funcionario::unidadeId)
                    .map(unidadeId -> listarPorUnidade(unidadeId, comando))
                    .orElseGet(() -> paginaVazia(comando));
        }

        return paginaVazia(comando);
    }

    private Pagina<Pedido> listarTodos(ListarPedidosComando comando) {
        CanalPedido canal = comando.canalPedido();
        Paginacao paginacao = comando.paginacao();

        return canal == null
                ? pedidoRepository.listarTodos(paginacao)
                : pedidoRepository.listarPorCanal(canal, paginacao);
    }

    private Pagina<Pedido> listarPorCliente(Id clienteId, ListarPedidosComando comando) {
        CanalPedido canal = comando.canalPedido();
        Paginacao paginacao = comando.paginacao();

        return canal == null
                ? pedidoRepository.listarPorClienteId(clienteId, paginacao)
                : pedidoRepository.listarPorClienteIdECanal(clienteId, canal, paginacao);
    }

    private Pagina<Pedido> listarPorUnidade(Id unidadeId, ListarPedidosComando comando) {
        CanalPedido canal = comando.canalPedido();
        Paginacao paginacao = comando.paginacao();

        return canal == null
                ? pedidoRepository.listarPorUnidadeId(unidadeId, paginacao)
                : pedidoRepository.listarPorUnidadeIdECanal(unidadeId, canal, paginacao);
    }

    private Pagina<Pedido> paginaVazia(ListarPedidosComando comando) {
        return new Pagina<>(List.of(), comando.paginacao().page(), comando.paginacao().size(), 0, 0);
    }
}
