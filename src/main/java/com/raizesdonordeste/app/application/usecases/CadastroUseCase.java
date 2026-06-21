package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.domain.comum.model.Email;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.*;
import com.raizesdonordeste.app.domain.identidade.repository.BaseRepository;
import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import com.raizesdonordeste.app.domain.identidade.repository.ContaRepository;
import com.raizesdonordeste.app.domain.identidade.services.SenhaHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CadastroUseCase implements CasoDeUso<CadastrarClienteComando, Id> {

    private static final String BASE_CLIENTES_SLUG = "clientes";
    private static final Role CLIENTE_ROLE = Role.CLIENTE;

    private final BaseRepository baseRepository;
    private final ClienteRepository clienteRepository;
    private final ContaRepository contaRepository;
    private final SenhaHasher senhaHasher;

    @Override
    @Transactional
    public Id executar(CadastrarClienteComando comando) {
        Base baseClientes = baseRepository.obterPorSlug(BASE_CLIENTES_SLUG)
                .orElseThrow(() -> new IllegalStateException("Base de clientes não foi encontrada."));

        if (!baseClientes.permiteRole(CLIENTE_ROLE)) {
            throw new IllegalStateException("Role cliente não é permitida nesta base.");
        }

        if (contaRepository.obterPorEmail(new Email(comando.email())).isPresent()) {
            throw new IllegalStateException("Conta já cadastrada.");
        }

        Conta conta = Conta.criar(
                baseClientes.getId(),
                comando.email(),
                comando.senha(),
                CLIENTE_ROLE,
                senhaHasher);

        Cliente cliente = Cliente.criar(
                conta.getId(),
                comando.nome(),
                comando.cpf(),
                comando.telefone(),
                comando.endereco(),
                comando.dataNascimento(),
                comando.versaoAceiteTermos()
        );

        contaRepository.inserir(conta);
        clienteRepository.inserir(cliente);

        return cliente.getId();
    }
}
