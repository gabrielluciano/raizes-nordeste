package com.raizesdonordeste.app.domain.pedido.services;

import com.raizesdonordeste.app.domain.comum.model.*;
import com.raizesdonordeste.app.domain.fidelidade.model.MovimentacaoPontos;
import com.raizesdonordeste.app.domain.fidelidade.model.RegrasFidelidade;
import com.raizesdonordeste.app.domain.fidelidade.model.TipoMovPontos;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.domain.organizacao.model.Unidade;
import com.raizesdonordeste.app.domain.pedido.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PedidoServiceTest {

    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        pedidoService = new PedidoService();
    }

    @Test
    void deveLancarExcecao_QuandoCriarPedidoCompletoComDadosNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> pedidoService.criarPedidoCompleto(null))
                .withMessage("dados não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoCriarPedidoCompletoComItensVazios() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> pedidoService.criarPedidoCompleto(
                        criarDadosNovoPedido().itens(Collections.emptyList()).build()))
                .withMessage("itens não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> pedidoService.criarPedidoCompleto(
                        criarDadosNovoPedido().itens(null).build()))
                .withMessage("itens não pode ser nulo ou vazio.");
    }

    @Test
    void deveCriarPedidoNormalmente_QuandoDadosValidos() {
        Pedido pedido = pedidoService.criarPedidoCompleto(criarDadosNovoPedido().build());
        assertThat(pedido).isNotNull();
        assertThat(pedido.getId()).isNotNull();
    }

    @Test
    void deveLancarExcecao_QuandoCalcularTotaisComPedidoNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> pedidoService.calcularTotais(
                        null, criarRegrasFidelidade().build(), Collections.emptySet(), null, 0))
                .withMessage("pedido não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoCalcularTotaisComRegrasNull() {
        Pedido pedido = pedidoService.criarPedidoCompleto(criarDadosNovoPedido().build());
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> pedidoService.calcularTotais(
                        pedido, null, Collections.emptySet(), null, 0))
                .withMessage("regras não pode ser nulo.");
    }

    @Test
    void deveIgnorarPontosDesejados_QuandoClienteFidelidadeNull() {
        DadosNovoPedido dados = criarDadosNovoPedido()
                .clienteFidelidade(null)
                .clienteVinculado(null)
                .funcionarioId(Id.aleatorio())
                .canal(CanalPedido.TOTEM)
                .build();
        Pedido pedido = pedidoService.criarPedidoCompleto(dados);

        ResultadoCalculo resultado = pedidoService.calcularTotais(
                pedido, criarRegrasFidelidade().build(), Collections.emptySet(), null, 10);

        assertThat(resultado.pontosConsumidos()).isZero();
        assertThat(resultado.valorDescontoPontos()).isEqualTo(Dinheiro.ZERO);
    }

    @Test
    void naoDeveMutarPedido_AntesDeChamarConsolidarTotais() {
        Pedido pedido = pedidoService.criarPedidoCompleto(criarDadosNovoPedido().build());
        Dinheiro valorTotalAntes = pedido.getValorTotal();

        pedidoService.calcularTotais(pedido, criarRegrasFidelidade().build(), Collections.emptySet(), null, 0);

        assertThat(pedido.getValorTotal()).isEqualTo(valorTotalAntes);
    }

    @Test
    void deveRetornarAcumulo_QuandoConsentimentoFidelizacaoESemResgate() {
        Cliente cliente = criarCliente().build();
        Pedido pedido = criarPedidoConsolidado(cliente, true, 0);
        RegrasFidelidade regras = criarRegrasFidelidade().build();
        ResultadoCalculo resultado = pedidoService.calcularTotais(pedido, regras, Collections.emptySet(), cliente, 0);
        pedido.consolidarTotais(resultado);

        Optional<MovimentacaoPontos> movimentacao = pedidoService.calcularAcumulo(cliente, pedido, regras);

        assertThat(movimentacao).isPresent();
        assertThat(movimentacao.get().getTipo()).isEqualTo(TipoMovPontos.ACUMULO);
    }

    @Test
    void deveRetornarResgate_QuandoClienteTemSaldoSuficienteEPontosDesejados() {
        Cliente cliente = criarCliente().saldoPontos(100).build();
        Pedido pedido = criarPedidoConsolidado(cliente, true, 10);
        RegrasFidelidade regras = criarRegrasFidelidade().build();
        ResultadoCalculo resultado = pedidoService.calcularTotais(pedido, regras, Collections.emptySet(), cliente, 10);
        pedido.consolidarTotais(resultado);

        Optional<MovimentacaoPontos> movimentacao = pedidoService.calcularResgate(resultado.pontosConsumidos(), cliente);

        assertThat(movimentacao).isPresent();
        assertThat(movimentacao.get().getTipo()).isEqualTo(TipoMovPontos.RESGATE);
    }

    @Test
    void deveRetornarEmpty_QuandoSemConsentimentoESemPontosConsumidos() {
        Cliente cliente = criarCliente().build();
        Pedido pedido = criarPedidoConsolidado(cliente, false, 0);
        RegrasFidelidade regras = criarRegrasFidelidade().build();
        ResultadoCalculo resultado = pedidoService.calcularTotais(pedido, regras, Collections.emptySet(), cliente, 0);
        pedido.consolidarTotais(resultado);

        assertThat(pedidoService.calcularAcumulo(cliente, pedido, regras)).isEmpty();
        assertThat(pedidoService.calcularResgate(resultado.pontosConsumidos(), cliente)).isEmpty();
    }

    @Test
    void deveCalcularAcumuloPontosCorretamente() {
        Cliente cliente = criarCliente().build();
        ItemPedido item = criarItemPedido().quantidade(2).precoUnitario(new Dinheiro(1000)).build();
        Pedido pedido = pedidoService.criarPedidoCompleto(
                criarDadosNovoPedido().clienteFidelidade(cliente).itens(List.of(item)).consentimentoFidelizacao(true).build());
        RegrasFidelidade regras = criarRegrasFidelidade().acumuloPorCentavo(BigDecimal.valueOf(0.1)).build();
        ResultadoCalculo resultado = pedidoService.calcularTotais(pedido, regras, Collections.emptySet(), cliente, 0);
        pedido.consolidarTotais(resultado);

        Optional<MovimentacaoPontos> movimentacao = pedidoService.calcularAcumulo(cliente, pedido, regras);

        assertThat(movimentacao).isPresent();
        assertThat(movimentacao.get().getTipo()).isEqualTo(TipoMovPontos.ACUMULO);
        assertThat(movimentacao.get().getPontos()).isEqualTo(200); // 2000 centavos * 0.1
    }

    private Pedido criarPedidoConsolidado(Cliente cliente, boolean consentimento, int pontosDesejados) {
        return pedidoService.criarPedidoCompleto(
                criarDadosNovoPedido().clienteFidelidade(cliente).consentimentoFidelizacao(consentimento).build());
    }

    private DadosNovoPedido.DadosNovoPedidoBuilder criarDadosNovoPedido() {
        return DadosNovoPedido.builder()
                .id(Id.aleatorio())
                .unidade(criarUnidade().build())
                .clienteFidelidade(criarCliente()
                        .nome("Cliente Fidelidade")
                        .cpf(new CPF("40397904053"))
                        .build())
                .clienteVinculado(criarCliente()
                        .nome("Cliente Vinculado")
                        .build())
                .funcionarioId(null)
                .nomeCliente("Cliente")
                .canal(CanalPedido.APP)
                .pickup(false)
                .horarioPedido(LocalDateTime.now())
                .consentimentoFidelizacao(true)
                .itens(List.of(criarItemPedido().build()))
                .promocoes(Collections.emptySet());
    }

    private Cliente.ClienteBuilder criarCliente() {
        return Cliente.builder()
                .id(Id.aleatorio())
                .contaId(Id.aleatorio())
                .nome("Nome")
                .cpf(new CPF("52998224725"))
                .telefone(new Telefone("11999999999"))
                .endereco("Endereco")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .saldoPontos(0)
                .aceiteTermos(true)
                .dataAceiteTermos(LocalDateTime.now())
                .versaoTermos("1.0")
                .dataCadastro(LocalDateTime.now());
    }

    private ItemPedido.ItemPedidoBuilder criarItemPedido() {
        return ItemPedido.builder()
                .id(Id.aleatorio())
                .pratoId(Id.aleatorio())
                .quantidade(1)
                .precoUnitario(new Dinheiro(1000));
    }

    private RegrasFidelidade.RegrasFidelidadeBuilder criarRegrasFidelidade() {
        return RegrasFidelidade.builder()
                .id(Id.aleatorio())
                .valorPorPonto(BigDecimal.ONE)
                .acumuloPorCentavo(BigDecimal.ONE)
                .validadePontosMeses(6)
                .tetoResgatePercentual(20);
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
