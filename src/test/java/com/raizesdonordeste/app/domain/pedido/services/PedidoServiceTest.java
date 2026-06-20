package com.raizesdonordeste.app.domain.pedido.services;

import com.raizesdonordeste.app.domain.cardapio.model.Prato;
import com.raizesdonordeste.app.domain.cardapio.model.Promocao;
import com.raizesdonordeste.app.domain.cardapio.repository.PratoRepository;
import com.raizesdonordeste.app.domain.cardapio.repository.PromocaoRepository;
import com.raizesdonordeste.app.domain.comum.model.CPF;
import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Telefone;
import com.raizesdonordeste.app.domain.fidelidade.model.MovimentacaoPontos;
import com.raizesdonordeste.app.domain.fidelidade.model.RegrasFidelidade;
import com.raizesdonordeste.app.domain.fidelidade.model.TipoMovPontos;
import com.raizesdonordeste.app.domain.fidelidade.repository.MovimentacaoPontosRepository;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import com.raizesdonordeste.app.domain.pedido.model.CanalPedido;
import com.raizesdonordeste.app.domain.pedido.model.DadosNovoPedido;
import com.raizesdonordeste.app.domain.pedido.model.ItemPedido;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PratoRepository pratoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PromocaoRepository promocaoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private MovimentacaoPontosRepository movimentacaoPontosRepository;

    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        pedidoService = new PedidoService(
                pratoRepository,
                clienteRepository,
                promocaoRepository,
                pedidoRepository,
                movimentacaoPontosRepository
        );
    }

    @Test
    void deveLancarExcecao_QuandoCriarPedidoCompletoComDadosNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pedidoService.criarPedidoCompleto(
                                null,
                                criarRegrasFidelidade().build(),
                                10))
                .withMessage("dados não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoCriarPedidoCompletoComRegrasNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pedidoService.criarPedidoCompleto(
                                criarDadosNovoPedido().build(),
                                null,
                                10))
                .withMessage("regras não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoCriarPedidoCompletoComItensVazios() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pedidoService.criarPedidoCompleto(
                                criarDadosNovoPedido()
                                        .itens(Collections.emptyList())
                                        .build(),
                                criarRegrasFidelidade().build(),
                                10))
                .withMessage("itens não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        pedidoService.criarPedidoCompleto(
                                criarDadosNovoPedido()
                                        .itens(null)
                                        .build(),
                                criarRegrasFidelidade().build(),
                                10))
                .withMessage("itens não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoPratoNaoPertenceAUnidadeDoPedido() {
        // Unidades
        Id unidadeIdPedido = Id.aleatorio();
        Id unidadeIdPrato = Id.aleatorio();

        // Prato de outra unidade
        Id pratoIdOutraUnidade = Id.aleatorio();
        Prato pratoOutraUnidade = criarPrato()
                .id(pratoIdOutraUnidade)
                .unidadeId(unidadeIdPrato)
                .build();

        // ItemPedido referenciando prato de outra unidade
        ItemPedido itemPedido = criarItemPedido().pratoId(pratoIdOutraUnidade).build();

        // Repository deve retornar o prato de outra unidade
        when(pratoRepository.obterPratosPorIds(anySet()))
                .thenReturn(Set.of(pratoOutraUnidade));

        DadosNovoPedido dados = criarDadosNovoPedido()
                .id(unidadeIdPedido)
                .itens(List.of(itemPedido))
                .build();

        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                pedidoService.criarPedidoCompleto(dados, criarRegrasFidelidade().build(), 10)
        ).withMessageContaining("pedido solicitado com prato não pertencente a unidade");
    }

    @Test
    void deveCriarNormalmente_QuandoTodosOsPratosPertencemAUnidade() {
        // Unidade
        Id unidadeIdPedido = Id.aleatorio();

        // Prato de mesma unidade
        Id pratoId = Id.aleatorio();
        Prato prato = criarPrato()
                .id(pratoId)
                .unidadeId(unidadeIdPedido)
                .build();

        // ItemPedido referenciando prato da mesma unidade
        ItemPedido itemPedido = criarItemPedido().pratoId(pratoId).build();

        // Repository deve retornar o prato da mesma unidade
        when(pratoRepository.obterPratosPorIds(anySet()))
                .thenReturn(Set.of(prato));

        // Outros mocks para prevenir falha do teste
        when(clienteRepository.obterPorId(any(Id.class)))
                .thenReturn(Optional.of(criarCliente().build()));
        when(promocaoRepository.obterPromocoesAtivasParaPratos(anySet()))
                .thenReturn(Collections.emptySet());

        DadosNovoPedido dados = criarDadosNovoPedido()
                .unidadeId(unidadeIdPedido)
                .id(unidadeIdPedido)
                .itens(List.of(itemPedido))
                .canal(CanalPedido.APP)
                .build();


        Pedido pedido = pedidoService.criarPedidoCompleto(dados, criarRegrasFidelidade().build(), 10);
        assertThat(pedido).isNotNull();
    }

    @Test
    void deveLancarExcecao_QuandoClienteIdNaoEncontradoNoRepositorio() {
        // Unidade
        Id unidadeIdPedido = Id.aleatorio();

        // Prato de mesma unidade
        Id pratoId = Id.aleatorio();
        Prato prato = criarPrato()
                .id(pratoId)
                .unidadeId(unidadeIdPedido)
                .build();

        // ItemPedido referenciando prato da mesma unidade
        ItemPedido itemPedido = criarItemPedido()
                .pratoId(pratoId).build();

        // Repository deve retornar o prato da mesma unidade
        when(pratoRepository.obterPratosPorIds(anySet()))
                .thenReturn(Set.of(prato));

        // Cliente não encontrado
        when(clienteRepository.obterPorId(any(Id.class)))
                .thenReturn(Optional.empty());

        DadosNovoPedido dados = criarDadosNovoPedido()
                .unidadeId(unidadeIdPedido)
                .id(unidadeIdPedido)
                .itens(List.of(itemPedido))
                .canal(CanalPedido.APP)
                .build();

        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                pedidoService.criarPedidoCompleto(dados, criarRegrasFidelidade().build(), 10)
        ).withMessageMatching("cliente de id '.*' não encontrado na base de pedidos.");
    }

    @Test
    void deveCriarPedidoSemFidelidade_QuandoClienteNaoEncontradoNoRepositorio() {
        // Unidade
        Id unidadeIdPedido = Id.aleatorio();

        // Prato de mesma unidade
        Id pratoId = Id.aleatorio();
        Prato prato = criarPrato()
                .id(pratoId)
                .unidadeId(unidadeIdPedido)
                .build();

        // ItemPedido referenciando prato da mesma unidade
        ItemPedido itemPedido = criarItemPedido()
                .pratoId(pratoId).build();

        // Repository deve retornar o prato da mesma unidade
        when(pratoRepository.obterPratosPorIds(anySet()))
                .thenReturn(Set.of(prato));

        // Cliente não encontrado
        when(clienteRepository.obterPorCpf(any(CPF.class)))
                .thenReturn(Optional.empty());

        DadosNovoPedido dados = criarDadosNovoPedido()
                .clienteId(null)
                .cpfCliente("52998224725")
                .unidadeId(unidadeIdPedido)
                .id(unidadeIdPedido)
                .funcionarioId(Id.aleatorio())
                .itens(List.of(itemPedido))
                .canal(CanalPedido.TOTEM)
                .build();

        Pedido pedido = pedidoService.criarPedidoCompleto(dados, criarRegrasFidelidade().build(), 5);

        assertThat(pedido).isNotNull();
        assertThat(pedido.getValorTotal()).isEqualTo(pedido.getValorFinal());
        assertThat(pedido.getValorDescontoPontos()).isEqualTo(Dinheiro.ZERO);
        verify(movimentacaoPontosRepository, never()).inserir(any());
        verify(clienteRepository, never()).atualizar(any());
    }

    @Test
    void deveCreditarPontosNoClienteEInserirMovimentacaoAcumuloEAtualizarCliente_QuandoFidelizacaoConsentidaESemResgate() {
        // Unidade
        Id unidadeIdPedido = Id.aleatorio();

        // Prato de mesma unidade
        Id pratoId = Id.aleatorio();
        Prato prato = criarPrato()
                .id(pratoId)
                .unidadeId(unidadeIdPedido)
                .build();

        // ItemPedido referenciando prato da mesma unidade
        ItemPedido itemPedido = criarItemPedido().pratoId(pratoId).build();

        // Repository deve retornar o prato da mesma unidade
        when(pratoRepository.obterPratosPorIds(anySet()))
                .thenReturn(Set.of(prato));

        // Cliente encontrado
        when(clienteRepository.obterPorId(any(Id.class)))
                .thenReturn(Optional.of(criarCliente().build()));
        when(promocaoRepository.obterPromocoesAtivasParaPratos(anySet()))
                .thenReturn(Collections.emptySet());

        DadosNovoPedido dados = criarDadosNovoPedido()
                .unidadeId(unidadeIdPedido)
                .id(unidadeIdPedido)
                .itens(List.of(itemPedido))
                .canal(CanalPedido.APP)
                .build();

        Pedido pedido = pedidoService.criarPedidoCompleto(dados, criarRegrasFidelidade().build(), 0);
        assertThat(pedido).isNotNull();

        ArgumentCaptor<MovimentacaoPontos> captor = ArgumentCaptor.forClass(MovimentacaoPontos.class);
        verify(movimentacaoPontosRepository, times(1)).inserir(captor.capture());
        assertThat(captor.getValue().getTipo()).isEqualTo(TipoMovPontos.ACUMULO);

        verify(clienteRepository, times(1)).atualizar(any());
        verify(pedidoRepository, times(1)).inserir(any());
    }

    @Test
    void naoDeveCreditarPontosNoCliente_QuandoSemConsentimentoMesmoComCliente() {
        // Unidade
        Id unidadeIdPedido = Id.aleatorio();

        // Prato de mesma unidade
        Id pratoId = Id.aleatorio();
        Prato prato = criarPrato()
                .id(pratoId)
                .unidadeId(unidadeIdPedido)
                .build();

        // ItemPedido referenciando prato da mesma unidade
        ItemPedido itemPedido = criarItemPedido().pratoId(pratoId).build();

        // Repository deve retornar o prato da mesma unidade
        when(pratoRepository.obterPratosPorIds(anySet()))
                .thenReturn(Set.of(prato));

        // Cliente encontrado
        when(clienteRepository.obterPorId(any(Id.class)))
                .thenReturn(Optional.of(criarCliente().build()));
        when(promocaoRepository.obterPromocoesAtivasParaPratos(anySet()))
                .thenReturn(Collections.emptySet());

        DadosNovoPedido dados = criarDadosNovoPedido()
                .unidadeId(unidadeIdPedido)
                .consentimentoFidelizacao(false)
                .id(unidadeIdPedido)
                .itens(List.of(itemPedido))
                .canal(CanalPedido.APP)
                .build();

        Pedido pedido = pedidoService.criarPedidoCompleto(dados, criarRegrasFidelidade().build(), 0);
        assertThat(pedido).isNotNull();

        verify(movimentacaoPontosRepository, never()).inserir(any());
        verify(clienteRepository, never()).atualizar(any());
        verify(pedidoRepository, times(1)).inserir(any());
    }

    @Test
    void deveDebitarPontosDoClienteEInserirMovimentacaoResgateEAtualizarCliente_QuandoFidelizacaoConsentidaEComResgate() {
        // Unidade
        Id unidadeIdPedido = Id.aleatorio();

        // Prato de mesma unidade
        Id pratoId = Id.aleatorio();
        Prato prato = criarPrato()
                .id(pratoId)
                .unidadeId(unidadeIdPedido)
                .build();

        // ItemPedido referenciando prato da mesma unidade
        ItemPedido itemPedido = criarItemPedido().pratoId(pratoId).build();

        // Repository deve retornar o prato da mesma unidade
        when(pratoRepository.obterPratosPorIds(anySet()))
                .thenReturn(Set.of(prato));

        // Cliente encontrado
        when(clienteRepository.obterPorId(any(Id.class)))
                .thenReturn(Optional.of(criarCliente().saldoPontos(100).build()));
        when(promocaoRepository.obterPromocoesAtivasParaPratos(anySet()))
                .thenReturn(Collections.emptySet());

        DadosNovoPedido dados = criarDadosNovoPedido()
                .unidadeId(unidadeIdPedido)
                .id(unidadeIdPedido)
                .itens(List.of(itemPedido))
                .canal(CanalPedido.APP)
                .build();

        Pedido pedido = pedidoService.criarPedidoCompleto(dados, criarRegrasFidelidade().build(), 10);
        assertThat(pedido).isNotNull();

        ArgumentCaptor<MovimentacaoPontos> captor = ArgumentCaptor.forClass(MovimentacaoPontos.class);
        verify(movimentacaoPontosRepository, times(1)).inserir(captor.capture());
        assertThat(captor.getValue().getTipo()).isEqualTo(TipoMovPontos.RESGATE);

        verify(clienteRepository, times(1)).atualizar(any());
        verify(pedidoRepository, times(1)).inserir(any());
    }

    @Test
    void deveCalcularAcumuloPontosCorretamente() {
        // Unidade
        Id unidadeIdPedido = Id.aleatorio();

        // Prato de mesma unidade
        Id pratoId = Id.aleatorio();
        Prato prato = criarPrato()
                .id(pratoId)
                .unidadeId(unidadeIdPedido)
                .build();

        // ItemPedido referenciando prato da mesma unidade
        ItemPedido itemPedido = criarItemPedido()
                .quantidade(2)
                .precoUnitario(new Dinheiro(1000))
                .pratoId(pratoId).build();

        // Repository deve retornar o prato da mesma unidade
        when(pratoRepository.obterPratosPorIds(anySet()))
                .thenReturn(Set.of(prato));

        // Cliente encontrado
        when(clienteRepository.obterPorId(any(Id.class)))
                .thenReturn(Optional.of(criarCliente().build()));
        when(promocaoRepository.obterPromocoesAtivasParaPratos(anySet()))
                .thenReturn(Collections.emptySet());

        DadosNovoPedido dados = criarDadosNovoPedido()
                .unidadeId(unidadeIdPedido)
                .id(unidadeIdPedido)
                .itens(List.of(itemPedido))
                .canal(CanalPedido.APP)
                .build();

        RegrasFidelidade regrasFidelidade = criarRegrasFidelidade()
                .pontosGanhosCentavos(0.1)
                .build();

        Pedido pedido = pedidoService.criarPedidoCompleto(dados, regrasFidelidade, 0);
        assertThat(pedido).isNotNull();

        ArgumentCaptor<MovimentacaoPontos> captor = ArgumentCaptor.forClass(MovimentacaoPontos.class);
        verify(movimentacaoPontosRepository, times(1)).inserir(captor.capture());
        assertThat(captor.getValue().getTipo()).isEqualTo(TipoMovPontos.ACUMULO);
        assertThat(captor.getValue().getPontos()).isEqualTo(200);
    }

    private DadosNovoPedido.DadosNovoPedidoBuilder criarDadosNovoPedido() {
        Id unidadeId = Id.aleatorio();
        return DadosNovoPedido.builder()
                .id(Id.aleatorio())
                .unidadeId(unidadeId)
                .clienteId(Id.aleatorio())
                .funcionarioId(null)
                .nomeCliente("Cliente")
                .canal(CanalPedido.APP)
                .pickup(false)
                .horarioPedido(LocalDateTime.now())
                .consentimentoFidelizacao(true)
                .itens(List.of(criarItemPedido().build()));
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

    private ItemPedido.ItemPedidoBuilder criarItemPedido() {
        return ItemPedido.builder()
                .id(Id.aleatorio())
                .pratoId(Id.aleatorio())
                .quantidade(1)
                .precoUnitario(new Dinheiro(1000));
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
                .pontosGanhosCentavos(1)
                .validadePontosMeses(6)
                .tetoResgatePercentual(20);
    }
}
