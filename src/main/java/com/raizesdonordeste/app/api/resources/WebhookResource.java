package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.ConfirmacaoPixRequest;
import com.raizesdonordeste.app.api.error.ErrorResponse;
import com.raizesdonordeste.app.application.usecases.ConfirmarPagamentoPixUseCase;
import com.raizesdonordeste.app.domain.pagamento.model.ConfirmarPagamentoPixComando;
import com.raizesdonordeste.app.infra.mapper.ConfirmarPagamentoPixComandoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping("webhooks")
@Tag(name = "Webhooks", description = "Recebimento de notificações de gateways externos")
public class WebhookResource {

    private final String webhookSecret;
    private final ConfirmarPagamentoPixUseCase confirmarPagamentoPixUseCase;
    private final ConfirmarPagamentoPixComandoMapper confirmarPagamentoPixComandoMapper;

    public WebhookResource(@Value("${app.gateway.webhook-secret}") String webhookSecret,
                           ConfirmarPagamentoPixUseCase confirmarPagamentoPixUseCase,
                           ConfirmarPagamentoPixComandoMapper confirmarPagamentoPixComandoMapper) {
        if (StringUtils.isEmpty(webhookSecret)) {
            throw new IllegalStateException("webhookSecret não pode ser vazio");
        }
        this.webhookSecret = webhookSecret;
        this.confirmarPagamentoPixUseCase = confirmarPagamentoPixUseCase;
        this.confirmarPagamentoPixComandoMapper = confirmarPagamentoPixComandoMapper;
    }

    @Operation(
            summary = "Confirmar pagamento PIX",
            description = "Recebe a confirmação de pagamento PIX do gateway. Autenticado pelo header X-Webhook-Secret."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Confirmação processada com sucesso.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload inválido ou header X-Webhook-Secret ausente.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Secret do webhook inválido.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @SecurityRequirements
    @PostMapping("pagamentos/pix")
    public ResponseEntity<Void> confirmarPagamentoPix(@Valid @RequestBody ConfirmacaoPixRequest request,
                                                      @RequestHeader("X-Webhook-Secret") String secret) {
        if (!MessageDigest.isEqual(secret.getBytes(UTF_8), webhookSecret.getBytes(UTF_8))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ConfirmarPagamentoPixComando comando = confirmarPagamentoPixComandoMapper.toComando(request);
        confirmarPagamentoPixUseCase.executar(comando);

        return ResponseEntity.ok().build();
    }
}
