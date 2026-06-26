package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.CadastroClienteRequest;
import com.raizesdonordeste.app.api.dto.CadastroResponse;
import com.raizesdonordeste.app.api.dto.LoginRequest;
import com.raizesdonordeste.app.api.dto.LoginResponse;
import com.raizesdonordeste.app.application.usecases.CadastroClienteUseCase;
import com.raizesdonordeste.app.application.usecases.LoginUseCase;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.CadastrarClienteComando;
import com.raizesdonordeste.app.domain.identidade.model.LoginComando;
import com.raizesdonordeste.app.domain.identidade.model.TokensAutenticacao;
import com.raizesdonordeste.app.infra.mapper.CadastrarClienteComandoMapper;
import com.raizesdonordeste.app.infra.mapper.LoginComandoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthResource {

    private static final String TOKEN_TYPE = "Bearer";

    private final CadastrarClienteComandoMapper cadastrarClienteComandoMapper;
    private final LoginComandoMapper loginComandoMapper;
    private final CadastroClienteUseCase cadastroClienteUseCase;
    private final LoginUseCase loginUseCase;

    @PostMapping("signup")
    public ResponseEntity<CadastroResponse> cadastrar(@Valid @RequestBody CadastroClienteRequest request) {
        CadastrarClienteComando comando = cadastrarClienteComandoMapper.toComando(request);
        Id id = cadastroClienteUseCase.executar(comando);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CadastroResponse(id.toString()));
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginComando comando = loginComandoMapper.toComando(request);
        TokensAutenticacao tokens = loginUseCase.executar(comando);
        return ResponseEntity.ok(new LoginResponse(
                tokens.accessToken(),
                tokens.refreshToken(),
                TOKEN_TYPE,
                tokens.accessTokenExpiraEm().toSeconds(),
                tokens.refreshTokenExpiraEm().toSeconds()
        ));
    }
}
