package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.domain.cardapio.model.ObterCardapioComando;
import com.raizesdonordeste.app.domain.cardapio.model.PratoVisualizacao;
import com.raizesdonordeste.app.domain.cardapio.repository.PratoRepository;
import com.raizesdonordeste.app.domain.comum.model.Pagina;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObterCardapioUseCase implements CasoDeUso<ObterCardapioComando, Pagina<PratoVisualizacao>> {

    private final PratoRepository pratoRepository;

    @Override
    public Pagina<PratoVisualizacao> executar(ObterCardapioComando comando) {
        return pratoRepository.obterPaginaDeAtivosEDisponiveisPorUnidadeId(
                        comando.unidadeId(), comando.paginacao())
                .mapear(PratoVisualizacao::fromPrato);
    }
}
