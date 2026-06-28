package com.raizesdonordeste.app.domain.pagamento.model;

import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Getter
public class Pagamento {

    private final Id id;
    private final Id pedidoId;
    private final String idempotencyKey;
    private final FormaPagamento forma;
    private StatusPagamento status;
    private final Dinheiro valor;
    private String idTransacaoGateway;
    private final LocalDateTime dataSolicitacao;
    private LocalDateTime dataConfirmacao;
    private String motivoRecusa;
    private String qrCode;
    private LocalDateTime qrCodeValidoAte;

    @Builder
    public Pagamento(Id id,
                     Id pedidoId,
                     String idempotencyKey,
                     FormaPagamento forma,
                     StatusPagamento status,
                     Dinheiro valor,
                     String idTransacaoGateway,
                     LocalDateTime dataSolicitacao,
                     LocalDateTime dataConfirmacao,
                     String motivoRecusa,
                     String qrCode,
                     LocalDateTime qrCodeValidoAte) {
        this.id = Guarda.naoNulo(id, "id");
        this.pedidoId = Guarda.naoNulo(pedidoId, "pedidoId");
        this.idempotencyKey = Guarda.naoVazio(idempotencyKey, "idempotencyKey");
        this.forma = Guarda.naoNulo(forma, "forma");
        this.status = Guarda.naoNulo(status, "status");
        this.valor = Guarda.positivo(valor, "valor");
        this.idTransacaoGateway = idTransacaoGateway;
        this.dataSolicitacao = Guarda.naoNulo(dataSolicitacao, "dataSolicitacao");
        this.dataConfirmacao = dataConfirmacao;
        this.motivoRecusa = motivoRecusa;
        this.qrCode = qrCode;
        this.qrCodeValidoAte = qrCodeValidoAte;
    }

    public static Pagamento criar(Id pedidoId,
                                  FormaPagamento forma,
                                  Dinheiro valor,
                                  LocalDateTime dataSolicitacao,
                                  String idempotencyKey) {
        return Pagamento.builder()
                .id(Id.aleatorio())
                .pedidoId(pedidoId)
                .forma(forma)
                .status(StatusPagamento.PENDENTE)
                .valor(valor)
                .dataSolicitacao(dataSolicitacao)
                .idempotencyKey(idempotencyKey)
                .build();
    }

    public void aprovar(String idTransacaoGateway, LocalDateTime dataConfirmacao) {
        if (!StatusPagamento.PENDENTE.equals(status)) {
            throw new IllegalStateException("só é possível aprovar um pedido pendente");
        }

        this.idTransacaoGateway = Guarda.naoVazio(idTransacaoGateway, "idTransacaoGateway");
        this.dataConfirmacao = Guarda.naoNulo(dataConfirmacao, "dataConfirmacao");
        this.status = StatusPagamento.APROVADO;
    }

    public void aprovar(LocalDateTime dataConfirmacao) {
        if (!StatusPagamento.PENDENTE.equals(status)) {
            throw new IllegalStateException("só é possível aprovar um pedido pendente");
        }

        this.status = StatusPagamento.APROVADO;
        this.dataConfirmacao = Guarda.naoNulo(dataConfirmacao, "dataConfirmacao");
    }

    public void registrarTransacao(String idTransacao) {
        this.idTransacaoGateway = idTransacao;
    }

    public void registrarQrCode(String qrCode, LocalDateTime qrCodeValidoAte) {
        boolean isPix = FormaPagamento.PIX.equals(forma);
        if (!isPix && (qrCode != null || qrCodeValidoAte != null)) {
            throw new IllegalArgumentException("informação de qrCode só deve ser fornecida para pagamento PIX");
        }

        if (isPix && (StringUtils.isBlank(qrCode) || qrCodeValidoAte == null)) {
            throw new IllegalArgumentException("qrCode e qrCodeValidoAte devem ser informados para pagamento PIX");
        }

        this.qrCode = qrCode;
        this.qrCodeValidoAte = qrCodeValidoAte;
    }

    public void recusar(String idTransacaoGateway, String motivoRecusa) {
        if (!StatusPagamento.PENDENTE.equals(status)) {
            throw new IllegalStateException("só é possível recusar um pedido pendente");
        }

        this.idTransacaoGateway = Guarda.naoVazio(idTransacaoGateway, "idTransacaoGateway");
        this.status = StatusPagamento.RECUSADO;
        this.motivoRecusa = Guarda.naoVazio(motivoRecusa, "motivoRecusa");
    }

    public void marcarErro(String motivoErro) {
        if (!StatusPagamento.PENDENTE.equals(status)) {
            throw new IllegalStateException("só é possível marcar erro em um pagamento pendente");
        }

        this.status = StatusPagamento.ERRO;
        this.motivoRecusa = Guarda.naoVazio(motivoErro, "motivoErro");
    }

    public boolean estaAprovado() {
        return StatusPagamento.APROVADO.equals(this.status);
    }

    public boolean estaPendente() {
        return StatusPagamento.PENDENTE.equals(this.status);
    }
}
