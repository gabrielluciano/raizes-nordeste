package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.application.services.AuditoriaService;
import com.raizesdonordeste.app.domain.auditoria.model.AtorTipo;
import com.raizesdonordeste.app.domain.auditoria.model.EventoAuditoria;
import com.raizesdonordeste.app.domain.auditoria.model.RegistroAuditoria;
import com.raizesdonordeste.app.domain.comum.model.Email;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.exceptions.ContaJaCadastradaException;
import com.raizesdonordeste.app.domain.identidade.model.CadastrarClienteComando;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.domain.identidade.model.Conta;
import com.raizesdonordeste.app.domain.identidade.model.Role;
import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import com.raizesdonordeste.app.domain.identidade.repository.ContaRepository;
import com.raizesdonordeste.app.domain.identidade.services.SenhaHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CadastroClienteUseCase implements CasoDeUso<CadastrarClienteComando, Id> {

    private final ClienteRepository clienteRepository;
    private final ContaRepository contaRepository;
    private final SenhaHasher senhaHasher;
    private final AuditoriaService auditoriaService;

    @Override
    @Transactional
    public Id executar(CadastrarClienteComando comando) {
        if (contaRepository.obterPorEmail(new Email(comando.email())).isPresent()) {
            throw new ContaJaCadastradaException();
        }

        Conta conta = Conta.criar(
                comando.email(),
                comando.senha(),
                Role.CLIENTE,
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

        auditoriaService.registrar(RegistroAuditoria.criar(
                AtorTipo.CLIENTE,
                conta.getId().toString(),
                EventoAuditoria.CLIENTE_CADASTRADO,
                "Cliente",
                cliente.getId().toString(),
                Map.of(
                        "email", comando.email(),
                        "versaoAceiteTermos", String.valueOf(comando.versaoAceiteTermos())
                )
        ));

        return cliente.getId();
    }
}
