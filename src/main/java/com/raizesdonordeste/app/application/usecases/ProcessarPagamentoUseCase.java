package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import com.raizesdonordeste.app.domain.identidade.repository.FuncionarioRepository;
import com.raizesdonordeste.app.domain.pagamento.exception.ErroPagamentoException;
import com.raizesdonordeste.app.domain.pagamento.model.Pagamento;
import com.raizesdonordeste.app.domain.pagamento.model.PagamentoComando;
import com.raizesdonordeste.app.domain.pagamento.model.ResultadoProcessamentoPagamento;
import com.raizesdonordeste.app.domain.pagamento.model.gateway.GatewayPagamento;
import com.raizesdonordeste.app.domain.pagamento.model.gateway.RespostaGatewayCartao;
import com.raizesdonordeste.app.domain.pagamento.repository.PagamentoRepository;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class ProcessarPagamentoUseCase extends AbstractPagamentoUseCase<ResultadoProcessamentoPagamento> {

    public ProcessarPagamentoUseCase(
            ClienteRepository clienteRepository,
            FuncionarioRepository funcionarioRepository,
            PedidoRepository pedidoRepository,
            PagamentoRepository pagamentoRepository,
            GatewayPagamento gatewayPagamento) {
        super(clienteRepository, funcionarioRepository, pedidoRepository, pagamentoRepository, gatewayPagamento);
    }

    @Override
    public ResultadoProcessamentoPagamento processar(PagamentoComando comando, Pedido pedido) {
        if (StringUtils.isBlank(comando.token())) {
            throw new ValidacaoException("token deve ser informado para pagamentos cartão");
        }

        Pagamento pagamento = Pagamento.criar(
                pedido.getId(),
                comando.formaPagamento(),
                pedido.getValorFinal(),
                LocalDateTime.now(),
                comando.idempotencyKey()
        );

        RespostaGatewayCartao resposta;
        try {
            resposta = gatewayPagamento.processarCartao(pagamento, comando.token());
        } catch (ErroPagamentoException e) {
            pagamento.marcarErro(e.getMessage());
            pagamentoRepository.inserir(pagamento);
            return criarRetorno(pagamento, pedido);
        }

        if (resposta.recusado()) {
            pagamento.recusar(resposta.id(), resposta.motivoRecusa());
            pagamentoRepository.inserir(pagamento);
        } else {
            pedido.avancarStatus();
            pagamento.aprovar(resposta.id(), resposta.pagoEm());
            pedidoRepository.atualizar(pedido);
            pagamentoRepository.inserir(pagamento);
        }

        return criarRetorno(pagamento, pedido);
    }

    @Override
    protected ResultadoProcessamentoPagamento criarRetorno(Pagamento pagamento, Pedido pedido) {
        return new ResultadoProcessamentoPagamento(
                pagamento.getId(),
                pagamento.getStatus(),
                pedido.getStatus(),
                pagamento.getMotivoRecusa()
        );
    }
}
