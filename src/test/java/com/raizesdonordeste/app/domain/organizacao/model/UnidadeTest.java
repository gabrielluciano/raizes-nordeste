package com.raizesdonordeste.app.domain.organizacao.model;

import com.raizesdonordeste.app.domain.comum.model.Horario;
import com.raizesdonordeste.app.domain.comum.model.Id;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UnidadeTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarUnidade()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComNomeBlank() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarUnidade()
                                .nome(null)
                                .build())
                .withMessage("nome não pode ser nulo ou vazio.");
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarUnidade()
                                .nome("")
                                .build())
                .withMessage("nome não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComEnderecoBlank() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarUnidade()
                                .endereco(null)
                                .build())
                .withMessage("endereco não pode ser nulo ou vazio.");
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarUnidade()
                                .endereco("")
                                .build())
                .withMessage("endereco não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComHorarioNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarUnidade()
                                .horarioFuncionamento(null)
                                .build())
                .withMessage("horarioFuncionamento não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComHorarioIntervaloInvalido() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarUnidade()
                                .horarioFuncionamento(new Horario(23, 10))
                                .build())
                .withMessage("intervalo de horário inválido");
    }

    @Test
    void deveRetornarTrue_QuandoEstaAberta() {
        Unidade unidade = criarUnidade().build();
        LocalDateTime agora = LocalDateTime.of(2018, Month.APRIL, 10, 15, 0, 0);

        assertThat(unidade.estaAberta(agora)).isTrue();
    }

    @Test
    void deveRetornarFalse_QuandoEstaFechada() {
        Unidade unidade = criarUnidade().build();
        LocalDateTime agora = LocalDateTime.of(2018, Month.APRIL, 10, 4, 0, 0);

        assertThat(unidade.estaAberta(agora)).isFalse();
    }

    private Unidade.UnidadeBuilder criarUnidade() {
        return Unidade.builder()
                .id(Id.aleatorio())
                .nome("Unidade")
                .endereco("Endereço")
                .horarioFuncionamento(new Horario(10, 23))
                .ativa(true);
    }
}
