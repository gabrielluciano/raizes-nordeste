package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.pagamento.exception.PagamentoNaoEncontradoException;
import com.raizesdonordeste.app.domain.pagamento.model.ConfirmarPagamentoPixComando;
import com.raizesdonordeste.app.domain.pagamento.model.Pagamento;
import com.raizesdonordeste.app.domain.pagamento.model.ResultadoConfirmacaoPix;
import com.raizesdonordeste.app.domain.pagamento.repository.PagamentoRepository;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfirmarPagamentoPixUseCase implements CasoDeUso<ConfirmarPagamentoPixComando, ResultadoConfirmacaoPix> {

    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;
    private final FidelidadeUseCase fidelidadeUseCase;

    @Override
    public ResultadoConfirmacaoPix executar(ConfirmarPagamentoPixComando comando) {

        Pagamento pagamento = pagamentoRepository.obterPorTransacaoGateway(comando.transacaoId())
                .orElseThrow(() -> PagamentoNaoEncontradoException.porIdTransacaoGateway(comando.transacaoId()));

        if (pagamento.estaAprovado()) {
            return criarResultado(pagamento);
        }

        if (!pagamento.estaPendente()) {
            throw new ValidacaoException("confirmação recebida para um pagamento que não está pendente");
        }

        pagamento.aprovar(comando.pagoEm());

        Pedido pedido = pedidoRepository.obterPorId(pagamento.getPedidoId())
                .orElseThrow(() -> new IllegalStateException("pagamento referencia um pedido não existente!"));
        pedido.avancarStatus();

        pagamentoRepository.atualizar(pagamento);
        pedidoRepository.atualizar(pedido);
        fidelidadeUseCase.executar(pedido.getId());

        return criarResultado(pagamento);
    }

    private ResultadoConfirmacaoPix criarResultado(Pagamento pagamento) {
        return new ResultadoConfirmacaoPix(
                pagamento.getId(),
                pagamento.getStatus()
        );
    }
}
