package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.CadastroFuncionarioRequest;
import com.raizesdonordeste.app.api.dto.CadastroResponse;
import com.raizesdonordeste.app.api.error.ErrorResponse;
import com.raizesdonordeste.app.application.usecases.CadastroFuncionarioUseCase;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.CadastrarFuncionarioComando;
import com.raizesdonordeste.app.infra.mapper.CadastrarFuncionarioComandoMapper;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
@Tag(name = "Administração de Funcionários", description = "Operações administrativas sobre funcionários")
public class AdminFuncionarioResource {

    private final CadastrarFuncionarioComandoMapper cadastrarFuncionarioComandoMapper;
    private final CadastroFuncionarioUseCase cadastroFuncionarioUseCase;

    @Operation(
            summary = "Cadastro de funcionário",
            description = "Cria a conta de um funcionário. Requer permissão administrativa."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Funcionário cadastrado com sucesso.",
                    content = @Content(schema = @Schema(implementation = CadastroResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de cadastro inválidos.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sem permissão para criar funcionários.",
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
    @PostMapping("funcionarios")
    @PreAuthorize("@regrasAutorizacao.podeCriarFuncionario(authentication)")
    public ResponseEntity<CadastroResponse> cadastrar(@Valid @RequestBody CadastroFuncionarioRequest request,
                                                      JwtAuthenticationToken authentication) {
        Jwt jwt = authentication.getToken();
        Id contaSolicitante = Id.fromString(jwt.getSubject());

        CadastrarFuncionarioComando comando = cadastrarFuncionarioComandoMapper.toComando(request, contaSolicitante);
        Id id = cadastroFuncionarioUseCase.executar(comando);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CadastroResponse(id.toString()));
    }
}
