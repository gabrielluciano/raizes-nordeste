package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.application.services.AuditoriaService;
import com.raizesdonordeste.app.domain.auditoria.model.AtorTipo;
import com.raizesdonordeste.app.domain.auditoria.model.EventoAuditoria;
import com.raizesdonordeste.app.domain.auditoria.model.RegistroAuditoria;
import com.raizesdonordeste.app.domain.comum.model.Email;
import com.raizesdonordeste.app.domain.identidade.exceptions.CredenciaisInvalidasException;
import com.raizesdonordeste.app.domain.identidade.model.*;
import com.raizesdonordeste.app.domain.identidade.repository.ContaRepository;
import com.raizesdonordeste.app.domain.identidade.repository.RefreshTokenRepository;
import com.raizesdonordeste.app.domain.identidade.services.ProvedorToken;
import com.raizesdonordeste.app.domain.identidade.services.SenhaHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginUseCase implements CasoDeUso<LoginComando, TokensAutenticacao> {

    private static final int DURACAO_ACCESS_TOKEN_MIN = 30;
    private static final int DURACAO_REFRESH_TOKEN_MIN = 120;

    private final ContaRepository contaRepository;
    private final SenhaHasher senhaHasher;
    private final ProvedorToken provedorToken;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuditoriaService auditoriaService;

    @Override
    public TokensAutenticacao executar(LoginComando comando) {
        Conta conta = contaRepository.obterPorEmail(new Email(comando.email()))
                .orElseThrow(() -> {
                    registrarFalha(null, comando.email(), "conta_inexistente");
                    return new CredenciaisInvalidasException();
                });

        if (!conta.verificarSenha(comando.senha(), senhaHasher)) {
            registrarFalha(conta.getId().toString(), comando.email(), "senha_invalida");
            throw new CredenciaisInvalidasException();
        }

        if (!StatusConta.ATIVA.equals(conta.getStatus())) {
            registrarFalha(conta.getId().toString(), comando.email(), "conta_inativa");
            throw new CredenciaisInvalidasException();
        }

        String accessToken = provedorToken.gerarAccessToken(conta, DURACAO_ACCESS_TOKEN_MIN);
        String refreshTokenPlano = provedorToken.gerarRefreshToken();
        RefreshToken refreshToken = RefreshToken.criar(
                conta.getId(),
                provedorToken.hashRefreshToken(refreshTokenPlano),
                Duration.ofMinutes(DURACAO_REFRESH_TOKEN_MIN)
        );
        refreshTokenRepository.inserir(refreshToken);

        auditoriaService.registrar(RegistroAuditoria.criar(
                conta.temRole(Role.CLIENTE) ? AtorTipo.CLIENTE : AtorTipo.FUNCIONARIO,
                conta.getId().toString(),
                EventoAuditoria.LOGIN_SUCESSO,
                "Conta",
                conta.getId().toString(),
                Map.of("email", comando.email())
        ));

        return new TokensAutenticacao(
                accessToken,
                refreshTokenPlano,
                Duration.ofMinutes(DURACAO_ACCESS_TOKEN_MIN),
                Duration.ofMinutes(DURACAO_REFRESH_TOKEN_MIN)
        );
    }

    private void registrarFalha(String contaId, String email, String motivo) {
        auditoriaService.registrar(RegistroAuditoria.criar(
                AtorTipo.SISTEMA,
                contaId,
                EventoAuditoria.LOGIN_FALHA,
                "Conta",
                contaId,
                Map.of("email", email, "motivo", motivo)
        ));
    }
}
