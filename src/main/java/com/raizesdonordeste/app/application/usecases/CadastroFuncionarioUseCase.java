package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.domain.comum.model.Email;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.CadastrarFuncionarioComando;
import com.raizesdonordeste.app.domain.identidade.model.Conta;
import com.raizesdonordeste.app.domain.identidade.model.Funcionario;
import com.raizesdonordeste.app.domain.identidade.model.Role;
import com.raizesdonordeste.app.domain.identidade.repository.ContaRepository;
import com.raizesdonordeste.app.domain.identidade.repository.FuncionarioRepository;
import com.raizesdonordeste.app.domain.identidade.services.SenhaHasher;
import com.raizesdonordeste.app.domain.organizacao.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CadastroFuncionarioUseCase implements CasoDeUso<CadastrarFuncionarioComando, Id> {

    private final FuncionarioRepository funcionarioRepository;
    private final UnidadeRepository unidadeRepository;
    private final ContaRepository contaRepository;
    private final SenhaHasher senhaHasher;

    @Override
    @Transactional
    public Id executar(CadastrarFuncionarioComando comando) {
        if (!Role.isFuncionario(comando.role())) {
            throw new IllegalArgumentException("Role inválida para funcionário: " + comando.role());
        }

        if (contaRepository.obterPorEmail(new Email(comando.email())).isPresent()) {
            throw new IllegalStateException("Conta já cadastrada.");
        }

        if (unidadeRepository.obterPorId(comando.unidadeId()).isEmpty()) {
            throw new IllegalArgumentException("Unidade não existente.");
        }

        Conta conta = Conta.criar(
                comando.email(),
                comando.senha(),
                comando.role(),
                senhaHasher);

        Funcionario funcionario = Funcionario.criar(
                conta.getId(),
                comando.unidadeId(),
                comando.nome(),
                comando.telefone(),
                comando.endereco(),
                comando.dataNascimento()
        );

        contaRepository.inserir(conta);
        funcionarioRepository.inserir(funcionario);

        return funcionario.getId();
    }
}
