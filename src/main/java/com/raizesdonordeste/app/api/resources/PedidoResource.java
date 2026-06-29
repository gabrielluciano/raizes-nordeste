package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.AvancarStatusPedidoResponse;
import com.raizesdonordeste.app.api.dto.CriarPedidoRequest;
import com.raizesdonordeste.app.api.dto.CriarPedidoResponse;
import com.raizesdonordeste.app.api.dto.PedidoResponse;
import com.raizesdonordeste.app.api.error.ErrorResponse;
import com.raizesdonordeste.app.application.usecases.AvancarStatusPedidoUseCase;
import com.raizesdonordeste.app.application.usecases.CriarPedidoUseCase;
import com.raizesdonordeste.app.application.usecases.ListarPedidosUseCase;
import com.raizesdonordeste.app.application.usecases.ObterPedidoUseCase;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Pagina;
import com.raizesdonordeste.app.domain.comum.model.Paginacao;
import com.raizesdonordeste.app.domain.identidade.model.Role;
import com.raizesdonordeste.app.domain.pedido.model.*;
import com.raizesdonordeste.app.infra.mapper.CriarPedidoComandoMapper;
import com.raizesdonordeste.app.infra.mapper.PedidoResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Endpoints para criação, consulta e atualização de pedidos.")
public class PedidoResource {

    private static final String ROLE_CLAIM = "role";

    private final CriarPedidoComandoMapper criarPedidoComandoMapper;
    private final CriarPedidoUseCase criarPedidoUseCase;
    private final ObterPedidoUseCase obterPedidoUseCase;
    private final ListarPedidosUseCase listarPedidosUseCase;
    private final PedidoResponseMapper pedidoResponseMapper;
    private final AvancarStatusPedidoUseCase avancarStatusPedidoUseCase;

    @Operation(
            summary = "Criar um novo pedido",
            description = "Cria um novo pedido para a conta autenticada."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pedido criado com sucesso.",
                    content = @Content(schema = @Schema(implementation = CriarPedidoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados do pedido inválidos, prato inativo ou inexistente.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sem permissão para criar pedidos.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    @PreAuthorize("@regrasAutorizacao.podeCriarPedido(authentication)")
    public ResponseEntity<CriarPedidoResponse> criar(@Valid @RequestBody CriarPedidoRequest request,
                                                     JwtAuthenticationToken authentication) {
        Jwt jwt = authentication.getToken();
        Id contaId = Id.fromString(jwt.getSubject());
        Role role = Role.valueOf(jwt.getClaimAsString(ROLE_CLAIM));

        CriarPedidoComando comando = criarPedidoComandoMapper.toComando(request, contaId, role);
        Id id = criarPedidoUseCase.executar(comando);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CriarPedidoResponse(id.toString()));
    }

    @Operation(
            summary = "Listar pedidos",
            description = "Lista de forma paginada os pedidos visíveis para a conta autenticada. " +
                    "Clientes veem apenas seus pedidos, funcionários veem os pedidos da própria unidade " +
                    "e administradores veem os pedidos de todas as unidades."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedidos listados com sucesso.",
                    content = @Content(schema = @Schema(implementation = Pagina.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetros de consulta inválidos.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping
    public Pagina<PedidoResponse> listarPedidos(@RequestParam(name = "canalPedido", required = false) CanalPedido canalPedido,
                                                @RequestParam(name = "page", required = false) Integer pageParam,
                                                @RequestParam(name = "size", required = false) Integer sizeParam,
                                                JwtAuthenticationToken authentication) {
        Jwt jwt = authentication.getToken();
        Id contaId = Id.fromString(jwt.getSubject());
        Role role = Role.valueOf(jwt.getClaimAsString(ROLE_CLAIM));

        int page = (pageParam == null || pageParam < 1) ? 1 : pageParam;
        int size = (sizeParam == null || sizeParam < 1) ? 10 : sizeParam;

        Paginacao paginacao = new Paginacao(page, size);
        ListarPedidosComando comando = new ListarPedidosComando(contaId, role, canalPedido, paginacao);

        return listarPedidosUseCase.executar(comando)
                .mapear(pedidoResponseMapper::toResponse);
    }

    @Operation(
            summary = "Obter pedido",
            description = "Retorna os detalhes de um pedido visível para a conta autenticada."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido retornado com sucesso.",
                    content = @Content(schema = @Schema(implementation = PedidoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Id de pedido inválido.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido não encontrado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("{id}")
    public ResponseEntity<PedidoResponse> obterPedido(@PathVariable String id,
                                                      JwtAuthenticationToken authentication) {
        Jwt jwt = authentication.getToken();
        Id contaId = Id.fromString(jwt.getSubject());
        Role role = Role.valueOf(jwt.getClaimAsString(ROLE_CLAIM));

        ObterPedidoComando comando = new ObterPedidoComando(Id.fromString(id), contaId, role);

        return obterPedidoUseCase.executar(comando)
                .map(pedidoResponseMapper::toResponse)
                .map(response -> ResponseEntity.ok().body(response))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Avançar status do pedido",
            description = "Avança o pedido para o próximo status."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Status do pedido avançado com sucesso.",
                    content = @Content(schema = @Schema(implementation = AvancarStatusPedidoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Id inválido, pedido não encontrado ou status não pode ser avançado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sem permissão para avançar o status do pedido.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping("{id}/avancar")
    @PreAuthorize("@regrasAutorizacao.podeAvancarStatusPedido(authentication)")
    public ResponseEntity<AvancarStatusPedidoResponse> avancarStatusPedido(@PathVariable String id,
                                                                           JwtAuthenticationToken authentication) {
        Jwt jwt = authentication.getToken();
        Id contaId = Id.fromString(jwt.getSubject());

        AvancarStatusPedidoComando comando = new AvancarStatusPedidoComando(contaId, Id.fromString(id));
        StatusPedido novoStatus = avancarStatusPedidoUseCase.executar(comando);

        return ResponseEntity.ok(new AvancarStatusPedidoResponse(novoStatus.name()));
    }
}
