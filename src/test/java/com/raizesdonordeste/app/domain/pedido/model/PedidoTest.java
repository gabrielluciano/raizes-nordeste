package com.raizesdonordeste.app.domain.pedido.model;

import com.raizesdonordeste.app.domain.cardapio.model.Prato;
import com.raizesdonordeste.app.domain.cardapio.model.Promocao;
import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.fidelidade.model.RegrasFidelidade;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PedidoTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComUnidadeIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .unidadeId(null)
                                .build())
                .withMessage("unidadeId não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComCanalNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .canal(null)
                                .build())
                .withMessage("canal não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComStatusNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .status(null)
                                .build())
                .withMessage("status não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComHorarioPedidoNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .horarioPedido(null)
                                .build())
                .withMessage("horarioPedido não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComValorTotalNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .valorTotal(null)
                                .build())
                .withMessage("valorTotal não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComValorTotalNegativo() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .valorTotal(new Dinheiro(-10))
                                .build())
                .withMessage("valorTotal valor não pode ser negativo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComValorDescontoPromocaoNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .valorDescontoPromocao(null)
                                .build())
                .withMessage("valorDescontoPromocao não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComValorDescontoPromocaoNegativo() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .valorDescontoPromocao(new Dinheiro(-10))
                                .build())
                .withMessage("valorDescontoPromocao valor não pode ser negativo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComValorDescontoPontosNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .valorDescontoPontos(null)
                                .build())
                .withMessage("valorDescontoPontos não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComValorDescontoPontosNegativo() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .valorDescontoPontos(new Dinheiro(-10))
                                .build())
                .withMessage("valorDescontoPontos valor não pode ser negativo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComValorFinalNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .valorFinal(null)
                                .build())
                .withMessage("valorFinal não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComValorFinalNegativo() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .valorFinal(new Dinheiro(-10))
                                .build())
                .withMessage("valorFinal valor não pode ser negativo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComCanalAppEClientIdNulo() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .canal(CanalPedido.APP)
                                .clienteId(null)
                                .build())
                .withMessage("clienteId deve ser informado em pedidos via APP.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComCanalDiferenteDeAppEClientIdPresente() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .canal(CanalPedido.TOTEM)
                                .clienteId(Id.aleatorio())
                                .build())
                .withMessage("clienteId só deve ser informado em pedidos via APP.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComCanalDiferenteDeAppENaoTemNome() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .canal(CanalPedido.TOTEM)
                                .clienteId(null)
                                .nomeCliente(null)
                                .build())
                .withMessage("nomeCliente deve ser informado quando o canal não é APP.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .canal(CanalPedido.TOTEM)
                                .clienteId(null)
                                .nomeCliente("")
                                .build())
                .withMessage("nomeCliente deve ser informado quando o canal não é APP.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComCanalAppEFuncionarioIdNulo() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido()
                                .canal(CanalPedido.TOTEM)
                                .clienteId(null)
                                .funcionarioId(null)
                                .build())
                .withMessage("funcionarioId deve ser informado quando não for APP.");
    }


    @Test
    void deveLancarExcecao_QuandoConsolidarTotaisEResultadoCalculoNulo() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPedido().build()
                                .consolidarTotais(null))
                .withMessage("resultado não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConsolidarTotaisEPagamentoNaoPendente() {
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                        criarPedido()
                                .status(StatusPedido.CONCLUIDO)
                                .build()
                                .consolidarTotais(
                                        new ResultadoCalculo(
                                                new Dinheiro(10),
                                                new Dinheiro(10),
                                                new Dinheiro(10),
                                                new Dinheiro(10),
                                                10
                                        )))
                .withMessage("totais só podem ser consolidados enquanto o pedido está em PAGAMENTO_PENDENTE.");
    }

    @Test
    void deveCalcularValorCheio_QuandoSemPromocoesEPontos() {
        Pedido pedido = criar(List.of(criarItemPedido()
                .quantidade(2)
                .precoUnitario(new Dinheiro(1000))
                .build()));

        ResultadoCalculo resultadoCalculo = pedido.calcularTotais(
                Collections.emptySet(), criarRegrasFidelidade().build(), 0, 0);

        assertThat(resultadoCalculo.valorTotal()).isEqualTo(new Dinheiro(2000));
        assertThat(resultadoCalculo.valorFinal()).isEqualTo(resultadoCalculo.valorTotal());
        assertThat(resultadoCalculo.valorDescontoPromocional()).isEqualTo(Dinheiro.ZERO);
        assertThat(resultadoCalculo.valorDescontoPontos()).isEqualTo(Dinheiro.ZERO);
        assertThat(resultadoCalculo.pontosConsumidos()).isEqualTo(0);
    }

    @Test
    void deveCalcularValorComDesconto_QuandoComPromocaoAplicavel() {
        ItemPedido itemPedido = criarItemPedido()
                .quantidade(2)
                .precoUnitario(new Dinheiro(1000))
                .build();
        Promocao promocao = criarPromocao()
                .pratoId(itemPedido.getPratoId())
                .percentualDesconto(20)
                .build();

        Pedido pedido = criar(List.of(itemPedido));

        ResultadoCalculo resultadoCalculo = pedido.calcularTotais(
                Set.of(promocao), criarRegrasFidelidade().build(), 0, 0);

        assertThat(resultadoCalculo.valorTotal()).isEqualTo(new Dinheiro(2000));
        assertThat(resultadoCalculo.valorFinal()).isEqualTo(new Dinheiro(1600));
        assertThat(resultadoCalculo.valorDescontoPromocional()).isEqualTo(new Dinheiro(400));
        assertThat(resultadoCalculo.valorDescontoPontos()).isEqualTo(Dinheiro.ZERO);
        assertThat(resultadoCalculo.pontosConsumidos()).isEqualTo(0);
    }

    @Test
    void deveCalcularValorSemDesconto_QuandoComPromocaoNaoAplicavel() {
        ItemPedido itemPedido = criarItemPedido()
                .quantidade(2)
                .precoUnitario(new Dinheiro(1000))
                .build();
        Promocao promocao = criarPromocao()
                .pratoId(Id.aleatorio())
                .percentualDesconto(20)
                .build();

        Pedido pedido = criar(List.of(itemPedido));

        ResultadoCalculo resultadoCalculo = pedido.calcularTotais(
                Set.of(promocao), criarRegrasFidelidade().build(), 0, 0);

        assertThat(resultadoCalculo.valorTotal()).isEqualTo(new Dinheiro(2000));
        assertThat(resultadoCalculo.valorFinal()).isEqualTo(resultadoCalculo.valorTotal());
        assertThat(resultadoCalculo.valorDescontoPromocional()).isEqualTo(Dinheiro.ZERO);
        assertThat(resultadoCalculo.valorDescontoPontos()).isEqualTo(Dinheiro.ZERO);
        assertThat(resultadoCalculo.pontosConsumidos()).isEqualTo(0);
    }

    @Test
    void deveCalcularValorComMaiorDesconto_QuandoMaisDeUmaPromocao() {
        ItemPedido itemPedido = criarItemPedido()
                .quantidade(1)
                .precoUnitario(new Dinheiro(2000))
                .build();
        Promocao promocaoMenor = criarPromocao()
                .pratoId(itemPedido.getPratoId())
                .percentualDesconto(5)
                .build();
        Promocao promocaoMaior = criarPromocao()
                .pratoId(itemPedido.getPratoId())
                .percentualDesconto(10)
                .build();

        Pedido pedido = criar(List.of(itemPedido));

        ResultadoCalculo resultadoCalculo = pedido.calcularTotais(
                Set.of(promocaoMenor, promocaoMaior), criarRegrasFidelidade().build(), 0, 0);

        assertThat(resultadoCalculo.valorTotal()).isEqualTo(new Dinheiro(2000));
        assertThat(resultadoCalculo.valorFinal()).isEqualTo(new Dinheiro(1800));
        assertThat(resultadoCalculo.valorDescontoPromocional()).isEqualTo(new Dinheiro(200));
        assertThat(resultadoCalculo.valorDescontoPontos()).isEqualTo(Dinheiro.ZERO);
        assertThat(resultadoCalculo.pontosConsumidos()).isEqualTo(0);
    }

    @Test
    void deveCalcularLimitarDescontoPromocionalA50PorCento() {
        // Invariante da promoção, mas testamos para garantir o comportamento correto no pedido
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarPromocao()
                                .percentualDesconto(60)
                                .build())
                .withMessage("percentual desconto deve estar entre 0 e 50.0");
    }

    @Test
    void deveCalcularValorComDesconto_QuandoResgatePontos() {
        Pedido pedido = criar(List.of(criarItemPedido()
                .quantidade(2)
                .precoUnitario(new Dinheiro(1000))
                .build()));
        RegrasFidelidade regrasFidelidade = criarRegrasFidelidade()
                .valorPonto(new Dinheiro(1))
                .tetoResgatePercentual(100)
                .build();

        ResultadoCalculo resultadoCalculo = pedido.calcularTotais(
                Collections.emptySet(), regrasFidelidade, 100, 100);

        assertThat(resultadoCalculo.valorTotal()).isEqualTo(new Dinheiro(2000));
        assertThat(resultadoCalculo.valorFinal()).isEqualTo(new Dinheiro(1900));
        assertThat(resultadoCalculo.valorDescontoPromocional()).isEqualTo(Dinheiro.ZERO);
        assertThat(resultadoCalculo.valorDescontoPontos()).isEqualTo(new Dinheiro(100));
        assertThat(resultadoCalculo.pontosConsumidos()).isEqualTo(100);
    }

    @Test
    void deveLimitarResgatePontosAoSaldo() {
        Pedido pedido = criar(List.of(criarItemPedido()
                .quantidade(2)
                .precoUnitario(new Dinheiro(1000))
                .build()));
        RegrasFidelidade regrasFidelidade = criarRegrasFidelidade()
                .valorPonto(new Dinheiro(1))
                .tetoResgatePercentual(100)
                .build();

        ResultadoCalculo resultadoCalculo = pedido.calcularTotais(
                Collections.emptySet(), regrasFidelidade, 100, 50);

        assertThat(resultadoCalculo.valorTotal()).isEqualTo(new Dinheiro(2000));
        assertThat(resultadoCalculo.valorFinal()).isEqualTo(new Dinheiro(1950));
        assertThat(resultadoCalculo.valorDescontoPromocional()).isEqualTo(Dinheiro.ZERO);
        assertThat(resultadoCalculo.valorDescontoPontos()).isEqualTo(new Dinheiro(50));
        assertThat(resultadoCalculo.pontosConsumidos()).isEqualTo(50);
    }

    @Test
    void deveLimitarResgatePontosAoTetoPercentual() {
        Pedido pedido = criar(List.of(criarItemPedido()
                .quantidade(1)
                .precoUnitario(new Dinheiro(1000))
                .build()));
        RegrasFidelidade regrasFidelidade = criarRegrasFidelidade()
                .valorPonto(new Dinheiro(1))
                .tetoResgatePercentual(20)
                .build();

        ResultadoCalculo resultadoCalculo = pedido.calcularTotais(
                Collections.emptySet(), regrasFidelidade, 250, 250);

        assertThat(resultadoCalculo.valorTotal()).isEqualTo(new Dinheiro(1000));
        assertThat(resultadoCalculo.valorFinal()).isEqualTo(new Dinheiro(800));
        assertThat(resultadoCalculo.valorDescontoPromocional()).isEqualTo(Dinheiro.ZERO);
        assertThat(resultadoCalculo.valorDescontoPontos()).isEqualTo(new Dinheiro(200));
        assertThat(resultadoCalculo.pontosConsumidos()).isEqualTo(200);
    }

    @Test
    void deveCalcularValorComDescontoPromocionalEPontos() {
        ItemPedido item1 = criarItemPedido()
                .quantidade(1)
                .precoUnitario(new Dinheiro(15000))
                .build();
        ItemPedido item2 = criarItemPedido()
                .quantidade(2)
                .precoUnitario(new Dinheiro(10000))
                .build();
        Pedido pedido = criar(List.of(item1, item2));
        Promocao promo1 = criarPromocao()
                .pratoId(item1.getPratoId())
                .percentualDesconto(1)
                .build();
        Promocao promo2 = criarPromocao()
                .pratoId(item2.getPratoId())
                .percentualDesconto(5)
                .build();
        Promocao promo3 = criarPromocao()
                .pratoId(Id.aleatorio())
                .percentualDesconto(5)
                .build();
        RegrasFidelidade regrasFidelidade = criarRegrasFidelidade()
                .valorPonto(new Dinheiro(2))
                .tetoResgatePercentual(10)
                .build();

        ResultadoCalculo resultadoCalculo = pedido.calcularTotais(
                Set.of(promo1, promo2, promo3), regrasFidelidade, 300, 400);

        assertThat(resultadoCalculo.valorTotal()).isEqualTo(new Dinheiro(35000));
        assertThat(resultadoCalculo.valorFinal()).isEqualTo(new Dinheiro(33250));
        assertThat(resultadoCalculo.valorDescontoPromocional()).isEqualTo(new Dinheiro(1150));
        assertThat(resultadoCalculo.valorDescontoPontos()).isEqualTo(new Dinheiro(600));
        assertThat(resultadoCalculo.pontosConsumidos()).isEqualTo(300);
    }

    private Pedido.PedidoBuilder criarPedido() {
        return Pedido.builder()
                .id(Id.aleatorio())
                .unidadeId(Id.aleatorio())
                .clienteId(Id.aleatorio())
                .funcionarioId(null)
                .nomeCliente("Cliente")
                .canal(CanalPedido.APP)
                .status(StatusPedido.PAGAMENTO_PENDENTE)
                .pickup(false)
                .horarioPedido(LocalDateTime.now())
                .consentimentoFidelizacao(false)
                .itens(List.of(criarItemPedido().build()))
                .valorTotal(new Dinheiro(1000))
                .valorDescontoPromocao(new Dinheiro(0))
                .valorDescontoPontos(new Dinheiro(0))
                .valorFinal(new Dinheiro(1000));
    }

    private Pedido criar(List<ItemPedido> itens) {
        return Pedido.criar(
                Id.aleatorio(),
                Id.aleatorio(),
                null,
                null,
                CanalPedido.APP,
                false,
                LocalDateTime.now(),
                true,
                itens
        );
    }

    private ItemPedido.ItemPedidoBuilder criarItemPedido() {
        return ItemPedido.builder()
                .id(Id.aleatorio())
                .pratoId(Id.aleatorio())
                .quantidade(1)
                .precoUnitario(new Dinheiro(1000));
    }

    private Prato.PratoBuilder criarPrato() {
        return Prato.builder()
                .id(Id.aleatorio())
                .unidadeId(Id.aleatorio())
                .nome("Nome")
                .descricao("Descricao")
                .preco(new Dinheiro(1000))
                .disponivel(true)
                .ativo(true);
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

    private RegrasFidelidade.RegrasFidelidadeBuilder criarRegrasFidelidade() {
        return RegrasFidelidade.builder()
                .valorPonto(new Dinheiro(1))
                .validadePontosMeses(6)
                .tetoResgatePercentual(20);
    }
}
