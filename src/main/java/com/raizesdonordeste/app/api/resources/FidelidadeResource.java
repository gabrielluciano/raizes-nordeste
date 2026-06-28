package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.ExtratoFidelidadeResponse;
import com.raizesdonordeste.app.api.error.ErrorResponse;
import com.raizesdonordeste.app.application.usecases.ObterExtratoFidelidadeUseCase;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.fidelidade.model.ExtratoFidelidade;
import com.raizesdonordeste.app.infra.mapper.ExtratoFidelidadeResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fidelidade")
@RequiredArgsConstructor
@Tag(name = "Fidelidade", description = "Programa de fidelidade do cliente")
public class FidelidadeResource {

    private final ObterExtratoFidelidadeUseCase obterExtratoFidelidadeUseCase;
    private final ExtratoFidelidadeResponseMapper extratoFidelidadeResponseMapper;

    @Operation(
            summary = "Obter extrato de fidelidade",
            description = "Retorna o extrato de pontos da conta autenticada."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Extrato obtido com sucesso.",
                    content = @Content(schema = @Schema(implementation = ExtratoFidelidadeResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sem permissão para consultar fidelidade.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("extrato")
    @PreAuthorize("@regrasAutorizacao.podeConsultarFidelidade(authentication)")
    public ResponseEntity<ExtratoFidelidadeResponse> obterExtrato(JwtAuthenticationToken authentication) {
        Jwt jwt = authentication.getToken();
        Id contaId = Id.fromString(jwt.getSubject());

        ExtratoFidelidade extrato = obterExtratoFidelidadeUseCase.executar(contaId);

        return ResponseEntity.ok(extratoFidelidadeResponseMapper.toResponse(extrato));
    }
}
