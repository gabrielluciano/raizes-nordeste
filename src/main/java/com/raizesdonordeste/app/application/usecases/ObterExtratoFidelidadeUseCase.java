package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.fidelidade.model.ExtratoFidelidade;
import com.raizesdonordeste.app.domain.fidelidade.model.MovimentacaoPontos;
import com.raizesdonordeste.app.domain.fidelidade.repository.MovimentacaoPontosRepository;
import com.raizesdonordeste.app.domain.identidade.exceptions.ClienteNaoEncontradoException;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObterExtratoFidelidadeUseCase implements CasoDeUso<Id, ExtratoFidelidade> {

    private final ClienteRepository clienteRepository;
    private final MovimentacaoPontosRepository movimentacaoPontosRepository;

    @Override
    @Transactional(readOnly = true)
    public ExtratoFidelidade executar(Id contaId) {
        Cliente cliente = clienteRepository.obterPorContaId(contaId)
                .orElseThrow(() -> new ClienteNaoEncontradoException(contaId));

        List<MovimentacaoPontos> movimentacoes = movimentacaoPontosRepository.obterMovimentacoesCliente(cliente.getId());

        return new ExtratoFidelidade(cliente.getSaldoPontos(), movimentacoes);
    }
}
