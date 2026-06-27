package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import com.raizesdonordeste.app.domain.identidade.repository.FuncionarioRepository;
import com.raizesdonordeste.app.domain.pagamento.exception.ErroPagamentoException;
import com.raizesdonordeste.app.domain.pagamento.model.Pagamento;
import com.raizesdonordeste.app.domain.pagamento.model.PagamentoComando;
import com.raizesdonordeste.app.domain.pagamento.model.ResultadoSolicitacaoPix;
import com.raizesdonordeste.app.domain.pagamento.model.gateway.GatewayPagamento;
import com.raizesdonordeste.app.domain.pagamento.model.gateway.RespostaGatewayPix;
import com.raizesdonordeste.app.domain.pagamento.repository.PagamentoRepository;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class IniciarPagamentoPixUseCase extends AbstractPagamentoUseCase<ResultadoSolicitacaoPix> {

    public IniciarPagamentoPixUseCase(
            ClienteRepository clienteRepository,
            FuncionarioRepository funcionarioRepository,
            PedidoRepository pedidoRepository,
            PagamentoRepository pagamentoRepository,
            GatewayPagamento gatewayPagamento) {
        super(clienteRepository, funcionarioRepository, pedidoRepository, pagamentoRepository, gatewayPagamento);
    }

    @Override
    public ResultadoSolicitacaoPix processar(PagamentoComando comando, Pedido pedido) {
        Pagamento pagamento = Pagamento.criar(
                pedido.getId(),
                comando.formaPagamento(),
                pedido.getValorFinal(),
                LocalDateTime.now(),
                comando.idempotencyKey()
        );
        RespostaGatewayPix resposta;
        try {
            resposta = gatewayPagamento.processarPix(pagamento);
        } catch (ErroPagamentoException e) {
            pagamento.marcarErro(e.getMessage());
            pagamentoRepository.inserir(pagamento);
            return criarRetorno(pagamento, pedido);
        }

        pagamento.registrarTransacao(resposta.id());
        pagamento.registrarQrCode(resposta.qrCode(), resposta.qrCodeValidoAte());
        pagamentoRepository.inserir(pagamento);

        return criarRetorno(pagamento, pedido);
    }

    @Override
    protected ResultadoSolicitacaoPix criarRetorno(Pagamento pagamento, Pedido pedido) {
        return new ResultadoSolicitacaoPix(
                pagamento.getId(),
                pagamento.getStatus(),
                pagamento.getQrCode(),
                pagamento.getQrCodeValidoAte()
        );
    }
}
