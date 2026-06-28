package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.ExtratoFidelidadeResponse;
import com.raizesdonordeste.app.application.usecases.ObterExtratoFidelidadeUseCase;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.fidelidade.model.ExtratoFidelidade;
import com.raizesdonordeste.app.infra.mapper.ExtratoFidelidadeResponseMapper;
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
public class FidelidadeResource {

    private final ObterExtratoFidelidadeUseCase obterExtratoFidelidadeUseCase;
    private final ExtratoFidelidadeResponseMapper extratoFidelidadeResponseMapper;

    @GetMapping("extrato")
    @PreAuthorize("@regrasAutorizacao.podeConsultarFidelidade(authentication)")
    public ResponseEntity<ExtratoFidelidadeResponse> obterExtrato(JwtAuthenticationToken authentication) {
        Jwt jwt = authentication.getToken();
        Id contaId = Id.fromString(jwt.getSubject());

        ExtratoFidelidade extrato = obterExtratoFidelidadeUseCase.executar(contaId);

        return ResponseEntity.ok(extratoFidelidadeResponseMapper.toResponse(extrato));
    }
}
