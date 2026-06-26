package com.raizesdonordeste.app.application.usecases;

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

@Service
@RequiredArgsConstructor
public class LoginUseCase implements CasoDeUso<LoginComando, TokensAutenticacao> {

    private static final int DURACAO_ACCESS_TOKEN_MIN = 30;
    private static final int DURACAO_REFRESH_TOKEN_MIN = 120;

    private final ContaRepository contaRepository;
    private final SenhaHasher senhaHasher;
    private final ProvedorToken provedorToken;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public TokensAutenticacao executar(LoginComando comando) {
        Conta conta = contaRepository.obterPorEmail(new Email(comando.email()))
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!conta.verificarSenha(comando.senha(), senhaHasher)) {
            throw new CredenciaisInvalidasException();
        }

        if (!StatusConta.ATIVA.equals(conta.getStatus())) {
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

        return new TokensAutenticacao(
                accessToken,
                refreshTokenPlano,
                Duration.ofMinutes(DURACAO_ACCESS_TOKEN_MIN),
                Duration.ofMinutes(DURACAO_REFRESH_TOKEN_MIN)
        );
    }
}
