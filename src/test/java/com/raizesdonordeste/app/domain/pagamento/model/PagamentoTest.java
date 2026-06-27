package com.raizesdonordeste.app.domain.pagamento.model;

import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PagamentoTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPagamento()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComPedidoIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPagamento()
                                .pedidoId(null)
                                .build())
                .withMessage("pedidoId não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdempotenceKeyBlank() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPagamento()
                                .idempotencyKey(null)
                                .build())
                .withMessage("idempotencyKey não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPagamento()
                                .idempotencyKey("")
                                .build())
                .withMessage("idempotencyKey não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComFormaNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPagamento()
                                .forma(null)
                                .build())
                .withMessage("forma não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComStatusNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPagamento()
                                .status(null)
                                .build())
                .withMessage("status não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComValorNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPagamento()
                                .valor(null)
                                .build())
                .withMessage("valor não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComValorNaoPositivo() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPagamento()
                                .valor(new Dinheiro(0))
                                .build())
                .withMessage("valor valor deve ser positivo.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPagamento()
                                .valor(new Dinheiro(-10))
                                .build())
                .withMessage("valor valor deve ser positivo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComDataSolicitacaoNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPagamento()
                                .dataSolicitacao(null)
                                .build())
                .withMessage("dataSolicitacao não pode ser nulo.");
    }

    @Test
    void deveAprovarPagamento_QuandoAprovar() {
        Pagamento pagamento = criarPagamento()
                .status(StatusPagamento.PENDENTE)
                .idTransacaoGateway(null)
                .dataConfirmacao(null)
                .build();
        LocalDateTime dataConfirmacao = LocalDateTime.now();
        String idAprovado = "idAprovado";

        pagamento.aprovar(idAprovado, dataConfirmacao);

        assertThat(pagamento.estaAprovado()).isTrue();
        assertThat(pagamento.getDataConfirmacao()).isEqualTo(dataConfirmacao);
        assertThat(pagamento.getIdTransacaoGateway()).isEqualTo(idAprovado);
    }

    @Test
    void deveAprovarPagamentoSemAlterarId_QuandoAprovar() {
        Pagamento pagamento = criarPagamento()
                .status(StatusPagamento.PENDENTE)
                .idTransacaoGateway(null)
                .dataConfirmacao(null)
                .build();
        LocalDateTime dataConfirmacao = LocalDateTime.now();
        String idAprovado = "idAprovado";

        pagamento.registrarTransacao(idAprovado);
        pagamento.aprovar(dataConfirmacao);

        assertThat(pagamento.estaAprovado()).isTrue();
        assertThat(pagamento.getDataConfirmacao()).isEqualTo(dataConfirmacao);
        assertThat(pagamento.getIdTransacaoGateway()).isEqualTo(idAprovado);
    }

    @Test
    void deveRecusarPagamento_QuandoRecusar() {
        Pagamento pagamento = criarPagamento()
                .status(StatusPagamento.PENDENTE)
                .idTransacaoGateway(null)
                .dataConfirmacao(null)
                .build();
        String idRecusado = "idRecusado";
        String motivoRecusa = "cartão inválido";

        pagamento.recusar(idRecusado, motivoRecusa);

        assertThat(pagamento.getStatus()).isEqualTo(StatusPagamento.RECUSADO);
        assertThat(pagamento.getIdTransacaoGateway()).isEqualTo(idRecusado);
        assertThat(pagamento.getDataConfirmacao()).isNull();
        assertThat(pagamento.getMotivoRecusa()).isEqualTo(motivoRecusa);
    }

    @Test
    void deveLancarExcecao_QuandoAprovarComIdPagamentoNaoPendente() {
        Pagamento pagamento = criarPagamento()
                .status(StatusPagamento.APROVADO)
                .build();

        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                        pagamento.aprovar("idAprovado", LocalDateTime.now()))
                .withMessage("só é possível aprovar um pedido pendente");
    }

    @Test
    void deveLancarExcecao_QuandoAprovarPagamentoNaoPendente() {
        Pagamento pagamento = criarPagamento()
                .status(StatusPagamento.RECUSADO)
                .build();

        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                        pagamento.aprovar(LocalDateTime.now()))
                .withMessage("só é possível aprovar um pedido pendente");
    }

    @Test
    void deveLancarExcecao_QuandoAprovarComIdTransacaoBlank() {
        Pagamento pagamento = criarPagamento()
                .status(StatusPagamento.PENDENTE)
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pagamento.aprovar("", LocalDateTime.now()))
                .withMessage("idTransacaoGateway não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoAprovarComIdEDataConfirmacaoNull() {
        Pagamento pagamento = criarPagamento()
                .status(StatusPagamento.PENDENTE)
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pagamento.aprovar("idAprovado", null))
                .withMessage("dataConfirmacao não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoAprovarComDataConfirmacaoNull() {
        Pagamento pagamento = criarPagamento()
                .status(StatusPagamento.PENDENTE)
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pagamento.aprovar(null))
                .withMessage("dataConfirmacao não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoRecusarPagamentoNaoPendente() {
        Pagamento pagamento = criarPagamento()
                .status(StatusPagamento.APROVADO)
                .build();

        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                        pagamento.recusar("idRecusado", "cartão inválido"))
                .withMessage("só é possível recusar um pedido pendente");
    }

    @Test
    void deveLancarExcecao_QuandoRecusarComIdTransacaoGatewayBlank() {
        Pagamento pagamento1 = criarPagamento()
                .status(StatusPagamento.PENDENTE)
                .build();

        Pagamento pagamento2 = criarPagamento()
                .status(StatusPagamento.PENDENTE)
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pagamento1.recusar(null, "cartão inválido"))
                .withMessage("idTransacaoGateway não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pagamento2.recusar("", "cartão inválido"))
                .withMessage("idTransacaoGateway não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoRecusarComMotivoBlank() {
        Pagamento pagamento1 = criarPagamento()
                .status(StatusPagamento.PENDENTE)
                .build();

        Pagamento pagamento2 = criarPagamento()
                .status(StatusPagamento.PENDENTE)
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pagamento1.recusar("idRecusado", null))
                .withMessage("motivoRecusa não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pagamento2.recusar("idRecusado", ""))
                .withMessage("motivoRecusa não pode ser nulo ou vazio.");
    }

    @Test
    void deveRegistrarQrCode_QuandoPagamentoPix() {
        Pagamento pagamento = criarPagamento()
                .forma(FormaPagamento.PIX)
                .build();
        LocalDateTime qrCodeValidoAte = LocalDateTime.now().plusMinutes(30);

        pagamento.registrarQrCode("qr-code", qrCodeValidoAte);

        assertThat(pagamento.getQrCode()).isEqualTo("qr-code");
        assertThat(pagamento.getQrCodeValidoAte()).isEqualTo(qrCodeValidoAte);
    }

    @Test
    void deveLancarExcecao_QuandoRegistrarQrCodeEmPagamentoNaoPix() {
        Pagamento pagamento = criarPagamento()
                .forma(FormaPagamento.CARTAO_CREDITO)
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pagamento.registrarQrCode("qr-code", LocalDateTime.now().plusMinutes(30)))
                .withMessage("informação de qrCode só deve ser fornecida para pagamento PIX");
    }

    @Test
    void deveLancarExcecao_QuandoRegistrarQrCodePixComQrCodeBlank() {
        Pagamento pagamento = criarPagamento()
                .forma(FormaPagamento.PIX)
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pagamento.registrarQrCode("", LocalDateTime.now().plusMinutes(30)))
                .withMessage("qrCode e qrCodeValidoAte devem ser informados para pagamento PIX");
    }

    @Test
    void deveLancarExcecao_QuandoRegistrarQrCodePixSemValidade() {
        Pagamento pagamento = criarPagamento()
                .forma(FormaPagamento.PIX)
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pagamento.registrarQrCode("qr-code", null))
                .withMessage("qrCode e qrCodeValidoAte devem ser informados para pagamento PIX");
    }

    @Test
    void deveRetornarPendente_QuandoStatusPendente() {
        Pagamento pagamento = criarPagamento()
                .status(StatusPagamento.PENDENTE)
                .build();

        assertThat(pagamento.estaPendente()).isTrue();
        assertThat(pagamento.estaAprovado()).isFalse();
    }

    private Pagamento.PagamentoBuilder criarPagamento() {
        return Pagamento.builder()
                .id(Id.aleatorio())
                .pedidoId(Id.aleatorio())
                .idempotencyKey("idempotencyKey")
                .forma(FormaPagamento.PIX)
                .status(StatusPagamento.PENDENTE)
                .valor(new Dinheiro(1000))
                .idTransacaoGateway("idTransacaoGateway")
                .dataSolicitacao(LocalDateTime.now())
                .dataConfirmacao(LocalDateTime.now().plusMinutes(1));
    }
}
