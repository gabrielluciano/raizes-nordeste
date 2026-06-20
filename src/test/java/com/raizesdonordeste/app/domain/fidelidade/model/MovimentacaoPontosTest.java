package com.raizesdonordeste.app.domain.fidelidade.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class MovimentacaoPontosTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarMovimentacaoPontos()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComClienteIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarMovimentacaoPontos()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComTipoNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarMovimentacaoPontos()
                                .tipo(null)
                                .build())
                .withMessage("tipo não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComPontosNaoPositivo() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarMovimentacaoPontos()
                                .pontos(0)
                                .build())
                .withMessage("pontos valor deve ser positivo.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarMovimentacaoPontos()
                                .pontos(-10)
                                .build())
                .withMessage("pontos valor deve ser positivo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComDataContabilizacaoNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarMovimentacaoPontos()
                                .dataContabilizacao(null)
                                .build())
                .withMessage("dataContabilizacao não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComTipoNaoAcumuloEDataExpiracaoNaoNull() {
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                        criarMovimentacaoPontos()
                                .tipo(TipoMovPontos.EXPIRACAO)
                                .dataExpiracao(LocalDateTime.now())
                                .build())
                .withMessage("dataExpiracao só pode ser informada para tipo ACUMULO.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComTipoAcumuloEDataExpiracaoNull() {
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                        criarMovimentacaoPontos()
                                .tipo(TipoMovPontos.ACUMULO)
                                .dataExpiracao(null)
                                .build())
                .withMessage("dataExpiracao deve ser informada para tipo ACUMULO.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComTipoAcumuloEDataExpiracaoNaoAposContabilizacao() {
        LocalDateTime now = LocalDateTime.now();
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                        criarMovimentacaoPontos()
                                .tipo(TipoMovPontos.ACUMULO)
                                .dataContabilizacao(now)
                                .dataExpiracao(now)
                                .build())
                .withMessage("dataExpiracao deve ser após dataContabilizacao.");

        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                        criarMovimentacaoPontos()
                                .tipo(TipoMovPontos.ACUMULO)
                                .dataContabilizacao(now.plusDays(10))
                                .dataExpiracao(now)
                                .build())
                .withMessage("dataExpiracao deve ser após dataContabilizacao.");
    }

    private MovimentacaoPontos.MovimentacaoPontosBuilder criarMovimentacaoPontos() {
        return MovimentacaoPontos.builder()
                .id(Id.aleatorio())
                .clienteId(Id.aleatorio())
                .pontos(10)
                .tipo(TipoMovPontos.ACUMULO)
                .dataContabilizacao(LocalDateTime.now())
                .dataExpiracao(LocalDateTime.now().plusMonths(6));
    }
}
