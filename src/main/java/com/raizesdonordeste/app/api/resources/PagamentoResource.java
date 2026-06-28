package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.PagamentoAssincronoResponse;
import com.raizesdonordeste.app.api.dto.PagamentoRequest;
import com.raizesdonordeste.app.api.dto.PagamentoSincronoResponse;
import com.raizesdonordeste.app.api.error.ErrorResponse;
import com.raizesdonordeste.app.application.usecases.IniciarPagamentoPixUseCase;
import com.raizesdonordeste.app.application.usecases.ProcessarPagamentoUseCase;
import com.raizesdonordeste.app.domain.pagamento.model.PagamentoComando;
import com.raizesdonordeste.app.domain.pagamento.model.ResultadoProcessamentoPagamento;
import com.raizesdonordeste.app.domain.pagamento.model.ResultadoSolicitacaoPix;
import com.raizesdonordeste.app.infra.mapper.PagamentoComandoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("pagamentos")
@RequiredArgsConstructor
@Tag(name = "Pagamentos", description = "Processamento de pagamentos de pedidos")
public class PagamentoResource {

    private final PagamentoComandoMapper pagamentoComandoMapper;
    private final ProcessarPagamentoUseCase processarPagamentoUseCase;
    private final IniciarPagamentoPixUseCase iniciarPagamentoPixUseCase;

    @Operation(
            summary = "Processar pagamento",
            description = "Processa um pagamento via cartão ou PIX. Exige o header Idempotency-Key."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pagamento com cartão processado.",
                    content = @Content(schema = @Schema(implementation = PagamentoSincronoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "202",
                    description = "Pagamento PIX iniciado.",
                    content = @Content(schema = @Schema(implementation = PagamentoAssincronoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de pagamento inválidos ou header Idempotency-Key ausente.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sem permissão para pagar.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    @PreAuthorize("@regrasAutorizacao.podePagar(authentication)")
    public ResponseEntity<?> processarPagamento(@Valid @RequestBody PagamentoRequest request,
                                                @RequestHeader(name = "Idempotency-Key") String idempotencyKey,
                                                JwtAuthenticationToken authentication) {
        Jwt jwt = authentication.getToken();
        String contaId = jwt.getSubject();

        PagamentoComando comando = pagamentoComandoMapper.toComando(request, contaId, idempotencyKey);
        return switch (request.getFormaPagamento()) {
            case PIX -> accepted()
                    .body(toPagamentoAssincronoResponse(iniciarPagamentoPixUseCase.executar(comando)));
            case CARTAO_DEBITO, CARTAO_CREDITO -> ok()
                    .body(toPagamentoSincronoResponse(processarPagamentoUseCase.executar(comando)));
        };
    }

    private PagamentoAssincronoResponse toPagamentoAssincronoResponse(ResultadoSolicitacaoPix resultado) {
        return new PagamentoAssincronoResponse(
                resultado.id().toString(),
                resultado.statusPagamento(),
                resultado.qrCode(),
                resultado.qrCodeValidoAte()
        );
    }

    private PagamentoSincronoResponse toPagamentoSincronoResponse(ResultadoProcessamentoPagamento resultado) {
        return new PagamentoSincronoResponse(
                resultado.id().toString(),
                resultado.statusPagamento(),
                resultado.statusPedido(),
                resultado.motivoRecusa()
        );
    }
}
