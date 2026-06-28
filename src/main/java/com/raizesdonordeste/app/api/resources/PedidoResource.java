package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.AvancarStatusPedidoResponse;
import com.raizesdonordeste.app.api.dto.CriarPedidoRequest;
import com.raizesdonordeste.app.api.dto.CriarPedidoResponse;
import com.raizesdonordeste.app.api.dto.PedidoResponse;
import com.raizesdonordeste.app.application.usecases.AvancarStatusPedidoUseCase;
import com.raizesdonordeste.app.application.usecases.CriarPedidoUseCase;
import com.raizesdonordeste.app.application.usecases.ObterPedidoUseCase;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.Role;
import com.raizesdonordeste.app.domain.pedido.model.AvancarStatusPedidoComando;
import com.raizesdonordeste.app.domain.pedido.model.CriarPedidoComando;
import com.raizesdonordeste.app.domain.pedido.model.ObterPedidoComando;
import com.raizesdonordeste.app.domain.pedido.model.StatusPedido;
import com.raizesdonordeste.app.infra.mapper.CriarPedidoComandoMapper;
import com.raizesdonordeste.app.infra.mapper.PedidoResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoResource {

    private static final String ROLE_CLAIM = "role";

    private final CriarPedidoComandoMapper criarPedidoComandoMapper;
    private final CriarPedidoUseCase criarPedidoUseCase;
    private final ObterPedidoUseCase obterPedidoUseCase;
    private final PedidoResponseMapper pedidoResponseMapper;
    private final AvancarStatusPedidoUseCase avancarStatusPedidoUseCase;

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

    @PatchMapping("/{id}/avancar")
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
