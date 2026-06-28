package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.application.services.AuditoriaService;
import com.raizesdonordeste.app.domain.auditoria.model.AtorTipo;
import com.raizesdonordeste.app.domain.auditoria.model.EventoAuditoria;
import com.raizesdonordeste.app.domain.auditoria.model.RegistroAuditoria;
import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import com.raizesdonordeste.app.domain.identidade.repository.FuncionarioRepository;
import com.raizesdonordeste.app.domain.pagamento.exception.ErroPagamentoException;
import com.raizesdonordeste.app.domain.pagamento.model.Pagamento;
import com.raizesdonordeste.app.domain.pagamento.model.PagamentoComando;
import com.raizesdonordeste.app.domain.pagamento.model.ResultadoSolicitacaoPix;
import com.raizesdonordeste.app.domain.pagamento.model.gateway.GatewayPagamento;
import com.raizesdonordeste.app.domain.pagamento.model.gateway.RespostaGatewayPix;
import com.raizesdonordeste.app.domain.pagamento.repository.PagamentoRepository;
import com.raizesdonordeste.app.domain.pedido.model.CanalPedido;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
public class IniciarPagamentoPixUseCase extends AbstractPagamentoUseCase<ResultadoSolicitacaoPix> {

    private final AuditoriaService auditoriaService;

    public IniciarPagamentoPixUseCase(
            ClienteRepository clienteRepository,
            FuncionarioRepository funcionarioRepository,
            PedidoRepository pedidoRepository,
            PagamentoRepository pagamentoRepository,
            GatewayPagamento gatewayPagamento,
            AuditoriaService auditoriaService) {
        super(clienteRepository, funcionarioRepository, pedidoRepository, pagamentoRepository, gatewayPagamento);
        this.auditoriaService = auditoriaService;
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
            registrarAuditoria(comando, pedido, pagamento, EventoAuditoria.PAGAMENTO_PIX_ERRO);
            return criarRetorno(pagamento, pedido);
        }

        pagamento.registrarTransacao(resposta.id());
        pagamento.registrarQrCode(resposta.qrCode(), resposta.qrCodeValidoAte());
        pagamentoRepository.inserir(pagamento);

        registrarAuditoria(comando, pedido, pagamento, EventoAuditoria.PAGAMENTO_PIX_INICIADO);

        return criarRetorno(pagamento, pedido);
    }

    private void registrarAuditoria(PagamentoComando comando, Pedido pedido, Pagamento pagamento, EventoAuditoria evento) {
        auditoriaService.registrar(RegistroAuditoria.criar(
                CanalPedido.APP.equals(pedido.getCanal()) ? AtorTipo.CLIENTE : AtorTipo.FUNCIONARIO,
                comando.contaId().toString(),
                evento,
                "Pagamento",
                pagamento.getId().toString(),
                Map.of(
                        "pedidoId", pedido.getId().toString(),
                        "status", pagamento.getStatus().name()
                )
        ));
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
