package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.application.services.AuditoriaService;
import com.raizesdonordeste.app.domain.auditoria.model.AtorTipo;
import com.raizesdonordeste.app.domain.auditoria.model.EventoAuditoria;
import com.raizesdonordeste.app.domain.auditoria.model.RegistroAuditoria;
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
import com.raizesdonordeste.app.domain.pedido.model.CanalPedido;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
public class ProcessarPagamentoUseCase extends AbstractPagamentoUseCase<ResultadoProcessamentoPagamento> {

    private final FidelidadeUseCase fidelidadeUseCase;
    private final AuditoriaService auditoriaService;

    public ProcessarPagamentoUseCase(
            ClienteRepository clienteRepository,
            FuncionarioRepository funcionarioRepository,
            PedidoRepository pedidoRepository,
            PagamentoRepository pagamentoRepository,
            GatewayPagamento gatewayPagamento,
            FidelidadeUseCase fidelidadeUseCase,
            AuditoriaService auditoriaService) {
        super(clienteRepository, funcionarioRepository, pedidoRepository, pagamentoRepository, gatewayPagamento);
        this.fidelidadeUseCase = fidelidadeUseCase;
        this.auditoriaService = auditoriaService;
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
            registrarAuditoria(comando, pedido, pagamento, EventoAuditoria.PAGAMENTO_ERRO);
            return criarRetorno(pagamento, pedido);
        }

        if (resposta.recusado()) {
            pagamento.recusar(resposta.id(), resposta.motivoRecusa());
            pagamentoRepository.inserir(pagamento);
            registrarAuditoria(comando, pedido, pagamento, EventoAuditoria.PAGAMENTO_RECUSADO);
        } else {
            pedido.confirmarPagamento();
            pagamento.aprovar(resposta.id(), resposta.pagoEm());
            pedidoRepository.atualizar(pedido);
            pagamentoRepository.inserir(pagamento);
            fidelidadeUseCase.executar(pedido.getId());
            registrarAuditoria(comando, pedido, pagamento, EventoAuditoria.PAGAMENTO_APROVADO);
        }

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
    protected ResultadoProcessamentoPagamento criarRetorno(Pagamento pagamento, Pedido pedido) {
        return new ResultadoProcessamentoPagamento(
                pagamento.getId(),
                pagamento.getStatus(),
                pedido.getStatus(),
                pagamento.getMotivoRecusa()
        );
    }
}
