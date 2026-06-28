package com.raizesdonordeste.app.infra.mapper;

import com.raizesdonordeste.app.api.dto.CadastroFuncionarioRequest;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.CadastrarFuncionarioComando;
import org.springframework.stereotype.Component;

@Component
public class CadastrarFuncionarioComandoMapper {

    public CadastrarFuncionarioComando toComando(CadastroFuncionarioRequest request, Id contaSolicitante) {
        return new CadastrarFuncionarioComando(
                contaSolicitante,
                request.getNome(),
                new Id(request.getUnidadeId()),
                request.getTelefone(),
                request.getEndereco(),
                request.getEmail(),
                request.getSenha(),
                request.getDataNascimento(),
                request.getRole()
        );
    }
}
