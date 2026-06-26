package com.raizesdonordeste.app.domain.pedido.services;

import com.raizesdonordeste.app.domain.cardapio.model.Promocao;
import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import com.raizesdonordeste.app.domain.fidelidade.model.MovimentacaoPontos;
import com.raizesdonordeste.app.domain.fidelidade.model.RegrasFidelidade;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.domain.organizacao.model.Unidade;
import com.raizesdonordeste.app.domain.pedido.model.DadosNovoPedido;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.model.ResultadoCalculo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static java.time.LocalDateTime.now;

public class PedidoService {

    public Pedido criarPedidoCompleto(DadosNovoPedido dados) {
        Guarda.naoNulo(dados, "dados");
        Guarda.naoVazio(dados.itens(), "itens");
        validaHorarioPreparo(dados.horarioPreparo(), dados.unidade());

        Cliente cliente = dados.clienteVinculado();

        return Pedido.criar(
                dados.unidade().getId(),
                cliente != null ? cliente.getId() : null,
                dados.funcionarioId(),
                dados.nomeCliente(),
                dados.canal(),
                dados.pickup(),
                dados.horarioPreparo(),
                dados.horarioPedido(),
                dados.consentimentoFidelizacao(),
                dados.itens()
        );
    }

    private void validaHorarioPreparo(LocalDateTime horarioPreparo, Unidade unidade) {
        if (horarioPreparo != null && !unidade.estaAberta(horarioPreparo)) {
            throw new ValidacaoException("horarioPreparo fora do horário de funcionamento da unidade");
        }
    }

    public ResultadoCalculo calcularTotais(Pedido pedido,
                                           RegrasFidelidade regras,
                                           Set<Promocao> promocoes,
                                           Cliente cliente,
                                           int pontosDesejados) {
        Guarda.naoNulo(pedido, "pedido");
        Guarda.naoNulo(regras, "regras");

        long saldoPontos = 0;
        long pontos = 0;

        if (cliente != null) {
            saldoPontos = cliente.getSaldoPontos();
            pontos = pontosDesejados;
        }

        return pedido.calcularTotais(promocoes, regras, pontos, saldoPontos);
    }

    public Optional<MovimentacaoPontos> calcularMovimentacaoPontos(long pontosConsumidos,
                                                                   Cliente cliente,
                                                                   Pedido pedido,
                                                                   RegrasFidelidade regras,
                                                                   Dinheiro valorFinal) {
        // TODO: Lógica de debitar pedidos deve ser movida para após pagamento
        if (pontosConsumidos == 0) {
            if (pedido.isConsentimentoFidelizacao()) {
                long acumulo = regras.acumuloPorCentavo()
                        .multiply(BigDecimal.valueOf(valorFinal.centavos()))
                        .setScale(0, RoundingMode.HALF_UP)
                        .longValue();
                return Optional.of(MovimentacaoPontos.acumulo(acumulo, cliente.getId(), now(), now().plusMonths(regras.validadePontosMeses())));
            }
        } else {
            return Optional.of(MovimentacaoPontos.resgate(pontosConsumidos, cliente.getId(), now()));
        }

        return Optional.empty();
    }
}
