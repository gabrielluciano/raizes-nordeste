package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.identidade.exceptions.AcessoNegadoException;
import com.raizesdonordeste.app.domain.identidade.exceptions.ClienteNaoEncontradoException;
import com.raizesdonordeste.app.domain.identidade.exceptions.FuncionarioNaoEncontradoException;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.domain.identidade.model.Funcionario;
import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import com.raizesdonordeste.app.domain.identidade.repository.FuncionarioRepository;
import com.raizesdonordeste.app.domain.pagamento.model.Pagamento;
import com.raizesdonordeste.app.domain.pagamento.model.PagamentoComando;
import com.raizesdonordeste.app.domain.pagamento.model.gateway.GatewayPagamento;
import com.raizesdonordeste.app.domain.pagamento.repository.PagamentoRepository;
import com.raizesdonordeste.app.domain.pedido.exceptions.PedidoNaoEncontradoException;
import com.raizesdonordeste.app.domain.pedido.model.CanalPedido;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractPagamentoUseCase<OUTPUT> implements CasoDeUso<PagamentoComando, OUTPUT> {

    protected final ClienteRepository clienteRepository;
    protected final FuncionarioRepository funcionarioRepository;
    protected final PedidoRepository pedidoRepository;
    protected final PagamentoRepository pagamentoRepository;
    protected final GatewayPagamento gatewayPagamento;

    @Override
    public OUTPUT executar(PagamentoComando comando) {
        Optional<Pagamento> pagamentoExistente = pagamentoRepository.obterPorIdempotencyKey(comando.idempotencyKey());

        if (pagamentoExistente.isPresent()) {
            Pagamento pagamento = pagamentoExistente.get();
            Pedido pedido = pedidoRepository.obterPorId(pagamento.getPedidoId())
                    .orElseThrow(() -> new IllegalStateException("pagamento referencia um pedido não existente!"));
            return criarRetorno(pagamento, pedido);
        }

        Pedido pedido = validarPagamento(comando);
        return processar(comando, pedido);
    }

    protected abstract OUTPUT processar(PagamentoComando comando, Pedido pedido);

    protected abstract OUTPUT criarRetorno(Pagamento pagamento, Pedido pedido);

    private Pedido validarPagamento(PagamentoComando comando) {
        Pedido pedido = pedidoRepository.obterPorId(comando.pedidoId())
                .orElseThrow(() -> new PedidoNaoEncontradoException(comando.pedidoId()));

        if (!pedido.permitePagamento()) {
            throw new ValidacaoException("pagamento só pode ser realizado para pedido com status PAGAMENTO_PENDENTE.");
        }

        if (CanalPedido.APP.equals(pedido.getCanal())) {
            Cliente cliente = clienteRepository.obterPorContaId(comando.contaId())
                    .orElseThrow(() -> new ClienteNaoEncontradoException(comando.contaId()));

            if (!Objects.equals(cliente.getId(), pedido.getClienteId())) {
                throw new AcessoNegadoException("cliente pagante diferente do cliente que realizou o pedido.");
            }
        } else {
            Funcionario funcionario = funcionarioRepository.obterPorContaId(comando.contaId())
                    .orElseThrow(() -> new FuncionarioNaoEncontradoException(comando.contaId()));

            if (!Objects.equals(funcionario.getUnidadeId(), pedido.getUnidadeId())) {
                throw new AcessoNegadoException("funcionário solicitante não pertence a unidade do pedido");
            }
        }

        return pedido;
    }
}
