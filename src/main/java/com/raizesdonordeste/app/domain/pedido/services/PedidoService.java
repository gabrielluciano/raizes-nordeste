package com.raizesdonordeste.app.domain.pedido.services;

import com.raizesdonordeste.app.domain.cardapio.model.Prato;
import com.raizesdonordeste.app.domain.cardapio.model.Promocao;
import com.raizesdonordeste.app.domain.cardapio.repository.PratoRepository;
import com.raizesdonordeste.app.domain.cardapio.repository.PromocaoRepository;
import com.raizesdonordeste.app.domain.comum.model.CPF;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import com.raizesdonordeste.app.domain.fidelidade.model.MovimentacaoPontos;
import com.raizesdonordeste.app.domain.fidelidade.model.RegrasFidelidade;
import com.raizesdonordeste.app.domain.fidelidade.repository.MovimentacaoPontosRepository;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import com.raizesdonordeste.app.domain.pedido.model.DadosNovoPedido;
import com.raizesdonordeste.app.domain.pedido.model.ItemPedido;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.model.ResultadoCalculo;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
public class PedidoService {

    private final PratoRepository pratoRepository;
    private final ClienteRepository clienteRepository;
    private final PromocaoRepository promocaoRepository;
    private final PedidoRepository pedidoRepository;
    private final MovimentacaoPontosRepository movimentacaoPontosRepository;

    public Pedido criarPedidoCompleto(DadosNovoPedido dados,
                                      RegrasFidelidade regras,
                                      int pontosDesejados) {
        Guarda.naoNulo(dados, "dados");
        Guarda.naoNulo(regras, "regras");
        Guarda.naoVazio(dados.itens(), "itens");

        List<ItemPedido> itens = dados.itens();
        Id unidadeId = dados.unidadeId();
        Set<Id> pratoIds = itens.stream().map(ItemPedido::getPratoId).collect(Collectors.toSet());

        validaPratosPertencemAUnidade(pratoIds, unidadeId);

        Pedido pedido = Pedido.criar(
                dados.unidadeId(),
                dados.clienteId(),
                dados.funcionarioId(),
                dados.nomeCliente(),
                dados.canal(),
                dados.pickup(),
                dados.horarioPedido(),
                dados.consentimentoFidelizacao(),
                dados.itens()
        );

        long saldoPontos = 0;
        long pontos = 0;

        Id clienteId = dados.clienteId();
        Cliente cliente = null;
        if (clienteId != null) {
            cliente = clienteRepository.obterPorId(clienteId)
                    .orElseThrow(() -> new IllegalStateException("cliente de id '%s' não encontrado na base de pedidos."
                            .formatted(clienteId)));

            saldoPontos = cliente.getSaldoPontos();
            pontos = pontosDesejados;
        } else if (!StringUtils.isBlank(dados.cpfCliente())) {
            cliente = clienteRepository.obterPorCpf(new CPF(dados.cpfCliente())).orElse(null);
        }

        if (cliente != null) {
            saldoPontos = cliente.getSaldoPontos();
            pontos = pontosDesejados;
        }

        Set<Promocao> promocoes = promocaoRepository.obterPromocoesAtivasParaPratos(pratoIds);

        ResultadoCalculo resultado = pedido.calcularTotais(promocoes, regras, pontos, saldoPontos);

        pedido.consolidarTotais(resultado);

        long pontosConsumidos = resultado.pontosConsumidos();

        // TODO: Lógica de debitar pedidos deve ser movida para após pagamento
        if (cliente != null) {
            if (pontosConsumidos == 0) {
                if (pedido.isConsentimentoFidelizacao()) {
                    long acumulo = Math.round(regras.pontosGanhosCentavos() * resultado.valorFinal().centavos());
                    MovimentacaoPontos movimentacaoPontos = MovimentacaoPontos.acumulo(acumulo, clienteId, now(), now().plusMonths(regras.validadePontosMeses()));
                    cliente.creditar(acumulo);
                    movimentacaoPontosRepository.inserir(movimentacaoPontos);
                    clienteRepository.atualizar(cliente);
                }
            } else {
                MovimentacaoPontos movimentacaoPontos = MovimentacaoPontos.resgate(pontosConsumidos, clienteId, now());
                movimentacaoPontosRepository.inserir(movimentacaoPontos);
                clienteRepository.atualizar(cliente);
            }

        }

        pedidoRepository.inserir(pedido);

        return pedido;
    }

    private void validaPratosPertencemAUnidade(Set<Id> pratoIds, Id unidadeId) {
        Set<Prato> pratos = pratoRepository.obterPratosPorIds(pratoIds);
        for (Prato prato : pratos) {
            if (!prato.pertenceAUnidade(unidadeId)) {
                throw new IllegalStateException("pedido solicitado com prato não pertencente a unidade." +
                        "pratoId: '%s', unidadeId: '%s'".formatted(prato.getId(), unidadeId));
            }
        }
    }
}
