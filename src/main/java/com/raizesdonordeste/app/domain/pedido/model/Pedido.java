package com.raizesdonordeste.app.domain.pedido.model;

import com.raizesdonordeste.app.domain.cardapio.model.Promocao;
import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import com.raizesdonordeste.app.domain.fidelidade.model.RegrasFidelidade;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Getter
public class Pedido {

    private final Id id;
    private final Id unidadeId;
    private final Id clienteId;
    private final Id funcionarioId;
    private final String nomeCliente;
    private final CanalPedido canal;
    private final StatusPedido status;
    private final boolean pickup;
    private final LocalDateTime horarioPreparo;
    private final LocalDateTime horarioPedido;
    private final boolean consentimentoFidelizacao;
    private Dinheiro valorTotal;
    private Dinheiro valorDescontoPromocao;
    private Dinheiro valorDescontoPontos;
    private Dinheiro valorFinal;

    @Getter
    private final List<ItemPedido> itensPedido;

    @Builder
    public Pedido(Id id,
                  Id unidadeId,
                  Id clienteId,
                  Id funcionarioId,
                  String nomeCliente,
                  CanalPedido canal,
                  StatusPedido status,
                  boolean pickup,
                  LocalDateTime horarioPedido,
                  LocalDateTime horarioPreparo,
                  boolean consentimentoFidelizacao,
                  List<ItemPedido> itens,
                  Dinheiro valorTotal,
                  Dinheiro valorDescontoPromocao,
                  Dinheiro valorDescontoPontos,
                  Dinheiro valorFinal) {
        this.id = Guarda.naoNulo(id, "id");
        this.unidadeId = Guarda.naoNulo(unidadeId, "unidadeId");
        this.clienteId = clienteId;
        this.funcionarioId = funcionarioId;
        this.nomeCliente = nomeCliente;
        this.canal = Guarda.naoNulo(canal, "canal");
        this.status = Guarda.naoNulo(status, "status");
        this.pickup = pickup;
        this.horarioPedido = Guarda.naoNulo(horarioPedido, "horarioPedido");
        this.horarioPreparo = horarioPreparo;
        this.consentimentoFidelizacao = consentimentoFidelizacao;
        this.itensPedido = List.copyOf(Guarda.naoVazio(itens, "itens"));
        this.valorTotal = Guarda.naoNegativo(valorTotal, "valorTotal");
        this.valorDescontoPromocao = Guarda.naoNegativo(valorDescontoPromocao, "valorDescontoPromocao");
        this.valorDescontoPontos = Guarda.naoNegativo(valorDescontoPontos, "valorDescontoPontos");
        this.valorFinal = Guarda.naoNegativo(valorFinal, "valorFinal");
        validarIdentificacaoCliente(clienteId, nomeCliente, canal);
        validarFuncionario(funcionarioId, canal);
        validarHorarioPreparo(horarioPreparo, horarioPedido, pickup, canal);
    }

    public static Pedido criar(Id unidadeId,
                               Id clienteId,
                               Id funcionarioId,
                               String nomeCliente,
                               CanalPedido canal,
                               boolean pickup,
                               LocalDateTime horarioPreparo,
                               LocalDateTime horarioPedido,
                               boolean consentimentoFidelizacao,
                               List<ItemPedido> itens) {
        return new Pedido(
                Id.aleatorio(),
                unidadeId,
                clienteId,
                funcionarioId,
                nomeCliente,
                canal,
                StatusPedido.PAGAMENTO_PENDENTE,
                pickup,
                horarioPedido,
                horarioPreparo,
                consentimentoFidelizacao,
                itens,
                new Dinheiro(0),
                new Dinheiro(0),
                new Dinheiro(0),
                new Dinheiro(0)
        );
    }

    private void validarIdentificacaoCliente(Id clienteId, String nomeCliente, CanalPedido canal) {
        boolean temNome = nomeCliente != null && !nomeCliente.isBlank();
        if (canal.equals(CanalPedido.APP)) {
            if (clienteId == null) {
                throw new ValidacaoException("clienteId deve ser informado em pedidos via APP.");
            }
        } else if (clienteId != null) {
            throw new ValidacaoException("clienteId só deve ser informado em pedidos via APP.");
        } else if (!temNome) {
            throw new ValidacaoException("nomeCliente deve ser informado quando o canal não é APP.");
        }
    }

    private void validarFuncionario(Id funcionarioId, CanalPedido canal) {
        if (!canal.equals(CanalPedido.APP) && funcionarioId == null) {
            throw new ValidacaoException("funcionarioId deve ser informado quando não for APP.");
        }
    }

    private void validarHorarioPreparo(LocalDateTime horarioPreparo, LocalDateTime horarioPedido, boolean pickup, CanalPedido canal) {
        if ((!pickup || !CanalPedido.APP.equals(canal)) && (horarioPreparo != null)) {
            throw new ValidacaoException("horarioPreparo só deve ser informado em pedidos pickup via APP.");
        }

        if (horarioPreparo != null && horarioPedido != null && !horarioPreparo.isAfter(horarioPedido)) {
            throw new ValidacaoException("horarioPreparo deve ser após horarioPedido.");
        }
    }

    public ResultadoCalculo calcularTotais(Set<Promocao> promocoes, RegrasFidelidade regrasFidelidade, long pontosDesejados, long saldoPontos) {
        Dinheiro total = calcularTotalSemDescontos();
        Dinheiro descontoPromocional = calcularDescontoPromocional(promocoes);
        Dinheiro valorComDescontoPromocao = total.subtrair(descontoPromocional);
        long pontosDisponiveis = Math.min(pontosDesejados, saldoPontos);
        Dinheiro valorDescontoPontos = calcularDescontoPontos(valorComDescontoPromocao, regrasFidelidade, pontosDisponiveis);
        long pontosConsumidos = calcularPontosConsumidos(valorDescontoPontos, regrasFidelidade, pontosDisponiveis);
        Dinheiro valorFinal = valorComDescontoPromocao.subtrair(valorDescontoPontos);

        return new ResultadoCalculo(
                total,
                valorFinal,
                descontoPromocional,
                valorDescontoPontos,
                pontosConsumidos
        );
    }

    private Dinheiro calcularTotalSemDescontos() {
        return itensPedido.stream()
                .map(ItemPedido::calcularSubtotal)
                .reduce(Dinheiro::somar)
                .orElse(new Dinheiro(0));
    }

    private Dinheiro calcularDescontoPromocional(Set<Promocao> promocoes) {
        Dinheiro descontoPromocional = new Dinheiro(0);
        for (ItemPedido item : this.itensPedido) {
            Dinheiro subtotal = item.calcularSubtotal();
            Id pratoId = item.getPratoId();

            Dinheiro descontoPromocionalItem = promocoes.stream()
                    .filter(promocao -> promocao.aplicaAoPrato(pratoId))
                    .map(promocao -> promocao.calcularDesconto(subtotal))
                    .max(Comparator.comparingLong(Dinheiro::centavos))
                    .orElse(new Dinheiro(0));

            descontoPromocional = descontoPromocional.somar(descontoPromocionalItem);
        }
        return descontoPromocional;
    }

    private Dinheiro calcularDescontoPontos(Dinheiro subTotal, RegrasFidelidade regras, long pontosDisponiveis) {
        Dinheiro descontoMaximoPermitido = subTotal.porcentagem(regras.tetoResgatePercentual());
        BigDecimal valorPorPonto = regras.valorPorPonto();
        long valorDosPontosCentavos = BigDecimal.valueOf(pontosDisponiveis)
                .multiply(valorPorPonto)
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();

        long centavosDescontoAplicado = Math.min(descontoMaximoPermitido.centavos(), valorDosPontosCentavos);

        return new Dinheiro(centavosDescontoAplicado);
    }

    private long calcularPontosConsumidos(Dinheiro valorDescontoPontos, RegrasFidelidade regras, long pontosDisponiveis) {
        BigDecimal valorPorPonto = regras.valorPorPonto();
        long pontosEquivalentes = BigDecimal.valueOf(valorDescontoPontos.centavos())
                .divide(valorPorPonto, 0, RoundingMode.FLOOR)
                .longValue();
        return Math.min(pontosEquivalentes, pontosDisponiveis);
    }

    public void consolidarTotais(ResultadoCalculo resultado) {
        Guarda.naoNulo(resultado, "resultado");
        if (this.status != StatusPedido.PAGAMENTO_PENDENTE) {
            throw new IllegalStateException("totais só podem ser consolidados enquanto o pedido está em PAGAMENTO_PENDENTE.");
        }
        this.valorTotal = resultado.valorTotal();
        this.valorDescontoPromocao = resultado.valorDescontoPromocional();
        this.valorDescontoPontos = resultado.valorDescontoPontos();
        this.valorFinal = resultado.valorFinal();
    }
}
