package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.fidelidade.model.RegrasFidelidade;
import com.raizesdonordeste.app.domain.fidelidade.repository.MovimentacaoPontosRepository;
import com.raizesdonordeste.app.domain.fidelidade.repository.RegrasFidelidadeRepository;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import com.raizesdonordeste.app.domain.pedido.exceptions.PedidoNaoEncontradoException;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import com.raizesdonordeste.app.domain.pedido.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FidelidadeUseCase implements CasoDeUso<Id, Void> {

    private final RegrasFidelidadeRepository regrasFidelidadeRepository;
    private final MovimentacaoPontosRepository movimentacaoPontosRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoService pedidoService = new PedidoService();

    @Override
    @Transactional
    public Void executar(Id pedidoId) {
        Pedido pedido = pedidoRepository.obterPorId(pedidoId)
                .orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));

        if (pedido.getClienteFidelidadeId() == null || !pedido.isConsentimentoFidelizacao()) {
            return null;
        }

        Cliente cliente = clienteRepository.obterPorId(pedido.getClienteFidelidadeId())
                .orElseThrow(() -> new IllegalStateException("pedido referencia cliente de fidelidade inexistente."));

        RegrasFidelidade regras = regrasFidelidadeRepository.obterAtiva();

        pedidoService.calcularAcumulo(cliente, pedido, regras)
                .ifPresent(movimentacao -> {
                    cliente.creditar(movimentacao.getPontos());
                    movimentacaoPontosRepository.inserir(movimentacao);
                    clienteRepository.atualizar(cliente);
                });

        return null;
    }
}
