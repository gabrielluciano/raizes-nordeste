package com.raizesdonordeste.app.domain.comum.model;

import java.time.LocalDateTime;

public record Horario(int horaDe, int horaAte) {

    public Horario {
        if (!horaValida(horaDe) || !horaValida(horaAte)) {
            throw new IllegalArgumentException("recebido hora inválida.");
        }

        if (horaDe >= horaAte) {
            throw new IllegalArgumentException("intervalo de horário inválido");
        }
    }

    private static boolean horaValida(int hora) {
        return !(hora < 0 || hora > 23);
    }

    public boolean estaDentro(LocalDateTime dataHora) {
        return dataHora.getHour() >= horaDe && dataHora.getHour() < horaAte;
    }
}
