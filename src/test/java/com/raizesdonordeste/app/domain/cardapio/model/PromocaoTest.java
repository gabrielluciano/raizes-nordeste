package com.raizesdonordeste.app.domain.cardapio.model;

import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PromocaoTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPromocao()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComPratoIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPromocao()
                                .pratoId(null)
                                .build())
                .withMessage("pratoId não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComDescricaoBlank() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPromocao()
                                .descricao(null)
                                .build())
                .withMessage("descricao não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPromocao()
                                .descricao("")
                                .build())
                .withMessage("descricao não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComDataHoraInicioNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPromocao()
                                .dataHoraInicio(null)
                                .build())
                .withMessage("dataHoraInicio não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComDataHoraFimNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPromocao()
                                .dataHoraFim(null)
                                .build())
                .withMessage("dataHoraFim não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComPercentualDescontoIgualAZeroOuNegativo() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPromocao()
                                .percentualDesconto(0)
                                .build())
                .withMessage("percentual desconto deve estar entre 0 e " + Promocao.DESCONTO_MAXIMO);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPromocao()
                                .percentualDesconto(-10)
                                .build())
                .withMessage("percentual desconto deve estar entre 0 e " + Promocao.DESCONTO_MAXIMO);
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComPercentualDescontoMaiorQueOPermitido() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPromocao()
                                .percentualDesconto(Promocao.DESCONTO_MAXIMO + 0.1)
                                .build())
                .withMessage("percentual desconto deve estar entre 0 e " + Promocao.DESCONTO_MAXIMO);
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComDataHoraInicioMenorOuIgualDataHoraFim() {
        LocalDateTime agora = LocalDateTime.now();
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                        criarPromocao()
                                .dataHoraInicio(agora)
                                .dataHoraFim(agora)
                                .build())
                .withMessage("dataHoraInicio deve ser antes de dataHoraFim");

        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                        criarPromocao()
                                .dataHoraInicio(agora)
                                .dataHoraFim(agora.minusDays(1))
                                .build())
                .withMessage("dataHoraInicio deve ser antes de dataHoraFim");
    }

    @Test
    void deveRetornarTrue_QuandoEstaVigente() {
        LocalDateTime agora = LocalDateTime.now();
        Promocao promocao = criarPromocao()
                .dataHoraInicio(agora)
                .dataHoraFim(agora.plusDays(1))
                .build();

        assertThat(promocao.estaVigente(agora.plusMinutes(5))).isTrue();
    }

    @Test
    void deveRetornarFalse_QuandoNaoEstaVigente() {
        LocalDateTime agora = LocalDateTime.now();
        Promocao promocao = criarPromocao()
                .dataHoraInicio(agora)
                .dataHoraFim(agora.plusDays(1))
                .build();

        assertThat(promocao.estaVigente(agora)).isFalse();
        assertThat(promocao.estaVigente(agora.plusDays(1))).isFalse();
        assertThat(promocao.estaVigente(agora.minusDays(1))).isFalse();
        assertThat(promocao.estaVigente(agora.plusDays(2))).isFalse();
    }

    @Test
    void deveLancarExcecao_QuandoCalcularDescontoComDinheiroNaoPositivo() {
        Promocao promocao = criarPromocao().build();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                        () -> promocao.calcularDesconto(new Dinheiro(0)))
                .withMessage("valor deve ser positivo");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                        () -> promocao.calcularDesconto(new Dinheiro(-10)))
                .withMessage("valor deve ser positivo");
    }

    @Test
    void deveCalcularDesconto() {
        Promocao promocao = criarPromocao()
                .percentualDesconto(10)
                .build();

        assertThat(promocao.calcularDesconto(new Dinheiro(100)).centavos()).isEqualTo(10);
        assertThat(promocao.calcularDesconto(new Dinheiro(1)).centavos()).isEqualTo(0);
        assertThat(promocao.calcularDesconto(new Dinheiro(950)).centavos()).isEqualTo(95);
    }

    @Test
    void deveDesativarPromocao() {
        Promocao promocao = criarPromocao()
                .ativa(true)
                .build();

        assertThat(promocao.estaAtiva()).isTrue();
        promocao.desativar();
        assertThat(promocao.estaAtiva()).isFalse();
    }

    private Promocao.PromocaoBuilder criarPromocao() {
        return Promocao.builder()
                .id(Id.aleatorio())
                .pratoId(Id.aleatorio())
                .descricao("Descricao")
                .percentualDesconto(10.0)
                .dataHoraInicio(LocalDateTime.now())
                .dataHoraFim(LocalDateTime.now().plusDays(1))
                .ativa(true);
    }
}
