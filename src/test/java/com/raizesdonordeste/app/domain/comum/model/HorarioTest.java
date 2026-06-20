package com.raizesdonordeste.app.domain.comum.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class HorarioTest {

    @Test
    void deveLancarExcecao_quandoHoraInvalida() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new Horario(2, 35)).withMessage("recebido hora inválida.");
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new Horario(2, -1)).withMessage("recebido hora inválida.");
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new Horario(35, 10)).withMessage("recebido hora inválida.");
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new Horario(-1, 10)).withMessage("recebido hora inválida.");
    }

    @Test
    void deveLancarExcecao_quandoIntervaloInvalido() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new Horario(10, 10)).withMessage("intervalo de horário inválido");
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new Horario(10, 5)).withMessage("intervalo de horário inválido");
    }

    @Test
    void deveConstruirCorretamente_quandoHorarioValido() {
        assertThatNoException().isThrownBy(() -> new Horario(10, 23));
    }

    @Test
    void deveRetornarTrue_quandoEstaDentroDoHorario() {
        LocalDateTime dataHora = LocalDateTime.of(2020, 1, 1, 11, 1);
        LocalDateTime dataHoraAbertura = LocalDateTime.of(2020, 1, 1, 10, 1);
        LocalDateTime dataHoraAntesFechamento = LocalDateTime.of(2020, 1, 1, 22, 59);
        Horario horario = new Horario(10, 23);

        assertThat(horario.estaDentro(dataHora)).isTrue();
        assertThat(horario.estaDentro(dataHoraAbertura)).isTrue();
        assertThat(horario.estaDentro(dataHoraAntesFechamento)).isTrue();
    }

    @Test
    void deveRetornarFalse_quandoHoraIgualAoFechamento() {
        LocalDateTime dataHoraFechamento = LocalDateTime.of(2020, 1, 1, 23, 0);
        Horario horario = new Horario(10, 23);

        assertThat(horario.estaDentro(dataHoraFechamento)).isFalse();
    }

    @Test
    void deveRetornarFalse_quandoEstaForaDoHorario() {
        LocalDateTime dataHoraAntesAbertura = LocalDateTime.of(2020, 1, 1, 8, 1);
        LocalDateTime dataHoraAposFechamento = LocalDateTime.of(2020, 1, 1, 23, 1);
        Horario horario = new Horario(10, 21);

        assertThat(horario.estaDentro(dataHoraAntesAbertura)).isFalse();
        assertThat(horario.estaDentro(dataHoraAposFechamento)).isFalse();
    }
}
