package com.raizesdonordeste.app.domain.identidade.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RefreshTokenTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarRefreshToken()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComContaIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarRefreshToken()
                                .contaId(null)
                                .build())
                .withMessage("contaId não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComTokenHashBlank() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarRefreshToken()
                                .tokenHash("")
                                .build())
                .withMessage("tokenHash não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarRefreshToken()
                                .tokenHash(null)
                                .build())
                .withMessage("tokenHash não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComExpiraEmNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarRefreshToken()
                                .expiraEm(null)
                                .build())
                .withMessage("expiraEm não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoCriarComDuracaoNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        RefreshToken.criar(Id.aleatorio(), "hash", null))
                .withMessage("duracao não pode ser nulo.");
    }

    @Test
    void deveCriarRefreshTokenComExpiracaoFutura() {
        Id contaId = Id.aleatorio();
        Duration duracao = Duration.ofDays(7);

        LocalDateTime antes = LocalDateTime.now();
        RefreshToken refreshToken = RefreshToken.criar(contaId, "hash", duracao);
        LocalDateTime depois = LocalDateTime.now();

        assertThat(refreshToken.getId()).isNotNull();
        assertThat(refreshToken.getContaId()).isEqualTo(contaId);
        assertThat(refreshToken.getTokenHash()).isEqualTo("hash");
        assertThat(refreshToken.getRevogadoEm()).isNull();
        assertThat(refreshToken.getExpiraEm())
                .isAfterOrEqualTo(antes.plus(duracao))
                .isBeforeOrEqualTo(depois.plus(duracao));
    }

    @Test
    void deveRevogarToken() {
        RefreshToken refreshToken = criarRefreshToken().build();

        assertThat(refreshToken.valido()).isTrue();
        refreshToken.revogar();
        assertThat(refreshToken.valido()).isFalse();
    }

    private RefreshToken.RefreshTokenBuilder criarRefreshToken() {
        return RefreshToken.builder()
                .id(Id.aleatorio())
                .contaId(Id.aleatorio())
                .tokenHash("hash")
                .expiraEm(LocalDateTime.now().plusDays(7))
                .revogadoEm(null);
    }
}
