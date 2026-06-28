package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.CadastroClienteRequest;
import com.raizesdonordeste.app.api.dto.CadastroResponse;
import com.raizesdonordeste.app.api.dto.LoginRequest;
import com.raizesdonordeste.app.api.dto.LoginResponse;
import com.raizesdonordeste.app.api.error.ErrorResponse;
import com.raizesdonordeste.app.application.usecases.CadastroClienteUseCase;
import com.raizesdonordeste.app.application.usecases.LoginUseCase;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.CadastrarClienteComando;
import com.raizesdonordeste.app.domain.identidade.model.LoginComando;
import com.raizesdonordeste.app.domain.identidade.model.TokensAutenticacao;
import com.raizesdonordeste.app.infra.mapper.CadastrarClienteComandoMapper;
import com.raizesdonordeste.app.infra.mapper.LoginComandoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticação e Cadastro", description = "Cadastro e login de clientes e funcionários")
public class AuthResource {

    private static final String TOKEN_TYPE = "Bearer";

    private final CadastrarClienteComandoMapper cadastrarClienteComandoMapper;
    private final LoginComandoMapper loginComandoMapper;
    private final CadastroClienteUseCase cadastroClienteUseCase;
    private final LoginUseCase loginUseCase;

    @Operation(
            summary = "Cadastro de cliente",
            description = "Cadastra um cliente e retorna o id gerado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cliente cadastrado com sucesso.",
                    content = @Content(schema = @Schema(implementation = CadastroResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de cadastro inválidos.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Já existe uma conta com o e-mail informado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @SecurityRequirements
    @PostMapping("signup")
    public ResponseEntity<CadastroResponse> cadastrar(@Valid @RequestBody CadastroClienteRequest request) {
        CadastrarClienteComando comando = cadastrarClienteComandoMapper.toComando(request);
        Id id = cadastroClienteUseCase.executar(comando);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CadastroResponse(id.toString()));
    }

    @Operation(
            summary = "Login de cliente/funcionários",
            description = "Permite um cliente ou funcionário se autenticar."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso.",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de login inválidos.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @SecurityRequirements
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
