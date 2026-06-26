package com.raizesdonordeste.app.domain.identidade.model;

import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Telefone;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FuncionarioTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarFuncionario()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComContaIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarFuncionario()
                                .contaId(null)
                                .build())
                .withMessage("contaId não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComUnidadeIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarFuncionario()
                                .unidadeId(null)
                                .build())
                .withMessage("unidadeId não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComNomeBlank() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarFuncionario()
                                .nome(null)
                                .build())
                .withMessage("nome não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarFuncionario()
                                .nome("")
                                .build())
                .withMessage("nome não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComTelefoneNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarFuncionario()
                                .telefone(null)
                                .build())
                .withMessage("telefone não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComEnderecoBlank() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarFuncionario()
                                .endereco(null)
                                .build())
                .withMessage("endereco não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarFuncionario()
                                .endereco("")
                                .build())
                .withMessage("endereco não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComDataNascimentoInvalida() {
        assertThatExceptionOfType(ValidacaoException.class).isThrownBy(() ->
                        criarFuncionario()
                                .dataNascimento(null)
                                .build())
                .withMessage("data de nascimento inválida, deve possuir ao menos '16' anos.");

        assertThatExceptionOfType(ValidacaoException.class).isThrownBy(() ->
                        criarFuncionario()
                                .dataNascimento(LocalDate.now())
                                .build())
                .withMessage("data de nascimento inválida, deve possuir ao menos '16' anos.");
    }

    @Test
    void deveRetornarTrue_QuandoPertenceAUnidade() {
        String semente = "semente";
        Id unidadeId = new Id(UUID.nameUUIDFromBytes(semente.getBytes()));
        var funcionario = criarFuncionario()
                .unidadeId(unidadeId)
                .build();

        Id mesmaUnidadeId = new Id(UUID.nameUUIDFromBytes(semente.getBytes()));
        assertThat(funcionario.pertenceAUnidade(mesmaUnidadeId)).isTrue();
    }

    @Test
    void deveRetornarFalse_QuandoNaoPertenceAUnidade() {
        String unidade1 = "unidade-1";
        String unidade2 = "unidade-2";
        Id unidadeId1 = new Id(UUID.nameUUIDFromBytes(unidade1.getBytes()));
        Id unidadeId2 = new Id(UUID.nameUUIDFromBytes(unidade2.getBytes()));

        var funcionario = criarFuncionario()
                .unidadeId(unidadeId1)
                .build();

        assertThat(funcionario.pertenceAUnidade(unidadeId2)).isFalse();
        assertThat(funcionario.pertenceAUnidade(null)).isFalse();
    }

    private Funcionario.FuncionarioBuilder criarFuncionario() {
        return Funcionario.builder()
                .id(Id.aleatorio())
                .contaId(Id.aleatorio())
                .unidadeId(Id.aleatorio())
                .nome("Nome")
                .telefone(new Telefone("11999999999"))
                .endereco("Endereço")
                .dataNascimento(LocalDate.of(2000, 1, 1));
    }
}
