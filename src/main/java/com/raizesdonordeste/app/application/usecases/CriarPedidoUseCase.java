package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.domain.cardapio.exceptions.PratoInativoException;
import com.raizesdonordeste.app.domain.cardapio.exceptions.PratoNaoEncontradoException;
import com.raizesdonordeste.app.domain.cardapio.model.Prato;
import com.raizesdonordeste.app.domain.cardapio.model.Promocao;
import com.raizesdonordeste.app.domain.cardapio.repository.PratoRepository;
import com.raizesdonordeste.app.domain.cardapio.repository.PromocaoRepository;
import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.comum.model.CPF;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.fidelidade.model.RegrasFidelidade;
import com.raizesdonordeste.app.domain.fidelidade.repository.MovimentacaoPontosRepository;
import com.raizesdonordeste.app.domain.fidelidade.repository.RegrasFidelidadeRepository;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.domain.identidade.model.Funcionario;
import com.raizesdonordeste.app.domain.identidade.model.Role;
import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import com.raizesdonordeste.app.domain.identidade.repository.FuncionarioRepository;
import com.raizesdonordeste.app.domain.organizacao.model.Unidade;
import com.raizesdonordeste.app.domain.organizacao.repository.UnidadeRepository;
import com.raizesdonordeste.app.domain.pedido.model.*;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import com.raizesdonordeste.app.domain.pedido.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CriarPedidoUseCase implements CasoDeUso<CriarPedidoComando, Id> {

    private final PedidoService pedidoService = new PedidoService();
    private final PratoRepository pratoRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final RegrasFidelidadeRepository regrasFidelidadeRepository;
    private final PromocaoRepository promocaoRepository;
    private final UnidadeRepository unidadeRepository;
    private final PedidoRepository pedidoRepository;
    private final MovimentacaoPontosRepository movimentacaoPontosRepository;

    @Override
    @Transactional
    public Id executar(CriarPedidoComando comando) {
        Identidade identidade = obterIdentidade(comando);
        Unidade unidade = obterUnidade(identidade.unidadeId());
        Cliente clienteVinculado = obterClienteVinculado(identidade.clienteId());
        Cliente clienteFidelidade = obterClienteFidelidade(clienteVinculado, comando.cpfCliente());
        List<ItemPedido> itens = obterItens(comando, identidade.unidadeId());
        Set<Promocao> promocoes = obterPromocoes(itens);

        DadosNovoPedido dados = DadosNovoPedido.builder()
                .unidade(unidade)
                .clienteVinculado(clienteVinculado)
                .clienteFidelidade(clienteFidelidade)
                .funcionarioId(identidade.funcionarioId())
                .cpfCliente(comando.cpfCliente())
                .nomeCliente(comando.nomeCliente())
                .canal(comando.canal())
                .pickup(comando.pickup())
                .horarioPedido(comando.horarioPedido() != null ? comando.horarioPedido() : LocalDateTime.now())
                .horarioPreparo(comando.horarioPreparo())
                .consentimentoFidelizacao(comando.consentimentoFidelizacao())
                .itens(itens)
                .promocoes(promocoes)
                .build();

        Pedido pedido = pedidoService.criarPedidoCompleto(dados);

        RegrasFidelidade regras = regrasFidelidadeRepository.obterAtiva();
        ResultadoCalculo resultado = pedidoService.calcularTotais(pedido, regras, dados.promocoes(), clienteFidelidade, comando.pontosDesejados());

        pedido.consolidarTotais(resultado);

        pedidoRepository.inserir(pedido);

        pedidoService.calcularResgate(resultado.pontosConsumidos(), clienteFidelidade)
                .ifPresent(movimentacao -> {
                    clienteFidelidade.debitar(movimentacao.pontos());
                    movimentacaoPontosRepository.inserir(movimentacao);
                    clienteRepository.atualizar(clienteFidelidade);
                });

        return pedido.getId();
    }

    private Identidade obterIdentidade(CriarPedidoComando comando) {
        if (Role.CLIENTE.equals(comando.role())) {
            if (comando.unidadeId() == null) {
                throw new ValidacaoException("unidadeId deve ser informada para pedidos via APP");
            }

            Cliente cliente = clienteRepository.obterPorContaId(comando.contaId())
                    .orElseThrow(() -> new IllegalStateException("cliente não encontrado para a conta '%s'.".formatted(comando.contaId())));
            return new Identidade(cliente.getId(), null, comando.unidadeId());
        }

        if (Role.isFuncionario(comando.role())) {
            Funcionario funcionario = funcionarioRepository.obterPorContaId(comando.contaId())
                    .orElseThrow(() -> new IllegalStateException("funcionário não encontrado para a conta '%s'.".formatted(comando.contaId())));
            return new Identidade(null, funcionario.id(), funcionario.unidadeId());
        }

        throw new IllegalStateException("role inválida recebida");
    }

    private Cliente obterClienteVinculado(Id clienteId) {
        if (clienteId == null) {
            return null;
        }

        return clienteRepository.obterPorId(clienteId)
                .orElseThrow(() -> new IllegalStateException("cliente de id '%s' não encontrado.".formatted(clienteId)));
    }

    private Cliente obterClienteFidelidade(Cliente clienteVinculado, String cpfCliente) {
        if (clienteVinculado != null) {
            return clienteVinculado;
        }

        if (!StringUtils.isBlank(cpfCliente)) {
            return clienteRepository.obterPorCpf(new CPF(cpfCliente)).orElse(null);
        }
        return null;
    }

    private List<ItemPedido> obterItens(CriarPedidoComando comando, Id unidadeId) {
        Set<Id> pratoIds = comando.itens().stream()
                .map(CriarPedidoComando.ItemComando::pratoId)
                .collect(Collectors.toSet());

        Set<Prato> pratos = pratoRepository.obterPratosPorIds(pratoIds);

        pratos.forEach(prato -> {
            if (!prato.isAtivo()) {
                throw new PratoInativoException(prato.getId());
            }
        });

        validaPratosPertencemAUnidade(pratos, unidadeId);

        Map<Id, Prato> pratosPorId = pratos.stream().collect(Collectors.toMap(Prato::getId, Function.identity()));

        return comando.itens().stream()
                .map(item -> {
                    Prato prato = pratosPorId.get(item.pratoId());
                    if (prato == null) {
                        throw new PratoNaoEncontradoException(item.pratoId());
                    }
                    return ItemPedido.de(prato, item.quantidade());
                })
                .toList();
    }

    private void validaPratosPertencemAUnidade(Set<Prato> pratos, Id unidadeId) {
        for (Prato prato : pratos) {
            if (!prato.pertenceAUnidade(unidadeId)) {
                throw new ValidacaoException("pedido solicitado com prato não pertencente a unidade." +
                        "pratoId: '%s', unidadeId: '%s'".formatted(prato.getId(), unidadeId));
            }
        }
    }

    private Set<Promocao> obterPromocoes(List<ItemPedido> itens) {
        Set<Id> pratoIds = itens.stream().map(ItemPedido::pratoId).collect(Collectors.toSet());
        return promocaoRepository.obterPromocoesAtivasParaPratos(pratoIds);
    }

    private Unidade obterUnidade(Id unidadeId) {
        return unidadeRepository.obterPorId(unidadeId)
                .orElseThrow(() -> new IllegalStateException("unidade de id '%s' não encontrada.".formatted(unidadeId)));
    }

    private record Identidade(Id clienteId, Id funcionarioId, Id unidadeId) {
    }
}
