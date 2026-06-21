package com.raizesdonordeste.app.domain.identidade.model;

import com.raizesdonordeste.app.domain.comum.model.Email;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.services.SenhaHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ContaTest {

    private static class TestSenhaHasher implements SenhaHasher {
        @Override
        public String gerarHash(String senha) {
            return "hash-" + senha;
        }

        @Override
        public boolean verificarSenha(String senha, String senhaHash) {
            return gerarHash(senha).equals(senhaHash);
        }
    }

    private TestSenhaHasher testSenhaHasher;

    @BeforeEach
    void setUp() {
        testSenhaHasher = new TestSenhaHasher();
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarConta()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComBaseIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarConta()
                                .baseId(null)
                                .build())
                .withMessage("baseId não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComEmailNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarConta()
                                .email(null)
                                .build())
                .withMessage("email não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComSenhaHashBlank() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarConta()
                                .senhaHash("")
                                .build())
                .withMessage("senhaHash não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarConta()
                                .senhaHash(null)
                                .build())
                .withMessage("senhaHash não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComStatusNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarConta()
                                .status(null)
                                .build())
                .withMessage("status não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComRoleNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarConta()
                                .role(null)
                                .build())
                .withMessage("role não pode ser nulo.");
    }

    @Test
    void deveRetornarTrue_quandoVerificarSenhaCorreta() {
        String senha = "senha";
        String senhaHash = testSenhaHasher.gerarHash(senha);
        Conta conta = criarConta().senhaHash(senhaHash).build();

        assertThat(conta.verificarSenha(senha, testSenhaHasher)).isTrue();
    }

    @Test
    void deveRetornarFalse_quandoVerificarSenhaIncorreta() {
        String senha = "valida";
        String senhaHash = testSenhaHasher.gerarHash(senha);
        Conta conta = criarConta().senhaHash(senhaHash).build();

        assertThat(conta.verificarSenha("invalida", testSenhaHasher)).isFalse();
    }

    @Test
    void deveTrocarSenha() {
        String senhaAntiga = "Antiga@23478!";
        Conta conta = criarConta().senhaHash(senhaAntiga).build();
        String senhaNova = "Nova#2233478!";

        conta.trocarSenha(senhaNova, testSenhaHasher);

        assertThat(conta.verificarSenha(senhaNova, testSenhaHasher)).isTrue();
    }

    @Test
    void deveLancarExcecao_quandoTrocarSenhaFraca() {
        Conta conta = criarConta().build();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        conta.trocarSenha("senha-fraca", testSenhaHasher))
                .withMessage("senha fraca recebida");
    }

    @Test
    void deveDesativarConta() {
        Conta conta = criarConta().build();

        assertThat(conta.getStatus()).isEqualTo(StatusConta.ATIVA);

        conta.desativar();

        assertThat(conta.getStatus()).isEqualTo(StatusConta.DESATIVADA);
    }

    @Test
    void deveRetornarFalse_quandoTemRoleComRoleNull() {
        Conta conta = criarConta().role(Role.COZINHA).build();

        assertThat(conta.temRole(null)).isFalse();
    }

    @Test
    void deveRetornarFalse_quandoTemRoleComRoleIncorreta() {
        Conta conta = criarConta().role(Role.COZINHA).build();

        assertThat(conta.temRole(Role.ADMINISTRADOR)).isFalse();
    }

    @Test
    void deveRetornarFalse_quandoTemRoleComRoleCorreta() {
        Conta conta = criarConta().role(Role.COZINHA).build();

        assertThat(conta.temRole(Role.COZINHA)).isTrue();
    }

    private Conta.ContaBuilder criarConta() {
        return Conta.builder()
                .id(Id.aleatorio())
                .baseId(Id.aleatorio())
                .email(new Email("email@example.com"))
                .senhaHash("hash")
                .status(StatusConta.ATIVA)
                .role(Role.COZINHA);
    }
}
