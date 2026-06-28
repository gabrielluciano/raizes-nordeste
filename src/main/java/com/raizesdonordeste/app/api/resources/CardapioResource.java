package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.PratoResponse;
import com.raizesdonordeste.app.api.error.ErrorResponse;
import com.raizesdonordeste.app.application.usecases.ObterCardapioUseCase;
import com.raizesdonordeste.app.domain.cardapio.model.ObterCardapioComando;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Pagina;
import com.raizesdonordeste.app.domain.comum.model.Paginacao;
import com.raizesdonordeste.app.infra.mapper.PratoResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Cardápio", description = "Consulta de cardápio")
public class CardapioResource {

    private final ObterCardapioUseCase obterCardapioUseCase;
    private final PratoResponseMapper pratoResponseMapper;

    @Operation(
            summary = "Obter cardápio da unidade",
            description = "Lista os pratos ativos e disponíveis da unidade de forma paginada."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cardápio retornado com sucesso.",
                    content = @Content(schema = @Schema(implementation = Pagina.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Id de unidade inválido.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @SecurityRequirements
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
