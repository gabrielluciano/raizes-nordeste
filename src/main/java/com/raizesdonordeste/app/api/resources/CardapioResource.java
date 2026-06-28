package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.PratoResponse;
import com.raizesdonordeste.app.application.usecases.ObterCardapioUseCase;
import com.raizesdonordeste.app.domain.cardapio.model.ObterCardapioComando;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Pagina;
import com.raizesdonordeste.app.domain.comum.model.Paginacao;
import com.raizesdonordeste.app.infra.mapper.PratoResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardapioResource {

    private final ObterCardapioUseCase obterCardapioUseCase;
    private final PratoResponseMapper pratoResponseMapper;

    @GetMapping("unidades/{unidadeId}/cardapio")
    public Pagina<PratoResponse> obterCardapio(@PathVariable String unidadeId,
                                               @RequestParam(name = "page", required = false) Integer pageParam,
                                               @RequestParam(name = "size", required = false) Integer sizeParam) {

        int page = (pageParam == null || pageParam < 1) ? 1 : pageParam;
        int size = (sizeParam == null || sizeParam < 1) ? 10 : sizeParam;

        Paginacao paginacao = new Paginacao(page, size);
        ObterCardapioComando comando = new ObterCardapioComando(Id.fromString(unidadeId), paginacao);

        return obterCardapioUseCase.executar(comando)
                .mapear(pratoResponseMapper::toResponse);
    }
}
