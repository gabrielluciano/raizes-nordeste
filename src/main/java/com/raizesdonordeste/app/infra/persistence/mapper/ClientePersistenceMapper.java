package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.comum.model.CPF;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Telefone;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.infra.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientePersistenceMapper {

    public ClienteEntity toEntity(Cliente cliente) {
        return new ClienteEntity(
                cliente.getId().id(),
                cliente.getContaId().id(),
                cliente.getNome(),
                cliente.getCpf().valor(),
                cliente.getTelefone().valor(),
                cliente.getEndereco(),
                cliente.getDataNascimento(),
                cliente.getSaldoPontos(),
                cliente.isAceiteTermos(),
                cliente.getDataAceiteTermos(),
                cliente.getVersaoTermos(),
                cliente.getDataCadastro()
        );
    }

    public Cliente toDomain(ClienteEntity entity) {
        return Cliente.builder()
                .id(new Id(entity.getId()))
                .contaId(new Id(entity.getContaId()))
                .nome(entity.getNome())
                .cpf(new CPF(entity.getCpf()))
                .telefone(new Telefone(entity.getTelefone()))
                .endereco(entity.getEndereco())
                .dataNascimento(entity.getDataNascimento())
                .saldoPontos(entity.getSaldoPontos())
                .aceiteTermos(entity.isAceiteTermos())
                .dataAceiteTermos(entity.getDataAceiteTermos())
                .versaoTermos(entity.getVersaoTermos())
                .dataCadastro(entity.getDataCadastro())
                .build();
    }
}
