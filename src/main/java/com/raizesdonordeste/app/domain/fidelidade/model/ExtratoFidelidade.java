package com.raizesdonordeste.app.domain.fidelidade.model;

import java.util.List;

public record ExtratoFidelidade(long saldoPontos, List<MovimentacaoPontos> movimentacoes) {
}
