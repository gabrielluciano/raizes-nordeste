package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.domain.identidade.model.Funcionario;
import com.raizesdonordeste.app.domain.identidade.model.Role;
import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import com.raizesdonordeste.app.domain.identidade.repository.FuncionarioRepository;
import com.raizesdonordeste.app.domain.pedido.model.ObterPedidoComando;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ObterPedidoUseCase implements CasoDeUso<ObterPedidoComando, Optional<Pedido>> {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;

    @Override
    public Optional<Pedido> executar(ObterPedidoComando comando) {
        return pedidoRepository.obterPorId(comando.pedidoId())
                .filter(pedido -> podeAcessar(pedido, comando));
    }

    private boolean podeAcessar(Pedido pedido, ObterPedidoComando comando) {
        Role role = comando.role();

        if (Role.ADMINISTRADOR.equals(role)) {
            return true;
        }

        if (Role.isCliente(role)) {
            return clienteRepository.obterPorContaId(comando.contaId())
                    .map(Cliente::getId)
                    .map(clienteId -> clienteId.equals(pedido.getClienteId()))
                    .orElse(false);
        }

        if (Role.isFuncionario(role)) {
            return funcionarioRepository.obterPorContaId(comando.contaId())
                    .map(Funcionario::unidadeId)
                    .map(unidadeId -> unidadeId.equals(pedido.getUnidadeId()))
                    .orElse(false);
        }

        return false;
    }
}
