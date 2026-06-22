package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.CadastroFuncionarioRequest;
import com.raizesdonordeste.app.api.dto.CadastroResponse;
import com.raizesdonordeste.app.application.usecases.CadastroFuncionarioUseCase;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.CadastrarFuncionarioComando;
import com.raizesdonordeste.app.infra.mapper.CadastrarFuncionarioComandoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminFuncionarioResource {

    private final CadastrarFuncionarioComandoMapper cadastrarFuncionarioComandoMapper;
    private final CadastroFuncionarioUseCase cadastroFuncionarioUseCase;

    @PostMapping("funcionarios")
    @PreAuthorize("@regrasAutorizacao.podeCriarFuncionario(authentication)")
    public ResponseEntity<CadastroResponse> cadastrar(@RequestBody CadastroFuncionarioRequest request) {
        CadastrarFuncionarioComando comando = cadastrarFuncionarioComandoMapper.toComando(request);
        Id id = cadastroFuncionarioUseCase.executar(comando);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CadastroResponse(id.toString()));
    }
}
