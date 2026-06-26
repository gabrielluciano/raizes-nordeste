package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.CriarPedidoRequest;
import com.raizesdonordeste.app.api.dto.CriarPedidoResponse;
import com.raizesdonordeste.app.application.usecases.CriarPedidoUseCase;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.Role;
import com.raizesdonordeste.app.domain.pedido.model.CriarPedidoComando;
import com.raizesdonordeste.app.infra.mapper.CriarPedidoComandoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoResource {

    private static final String ROLE_CLAIM = "role";

    private final CriarPedidoComandoMapper criarPedidoComandoMapper;
    private final CriarPedidoUseCase criarPedidoUseCase;

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
}
