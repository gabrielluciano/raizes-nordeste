package com.raizesdonordeste.app.domain.identidade.model;

import com.raizesdonordeste.app.domain.comum.model.CPF;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Telefone;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ClienteTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarCliente()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComContaIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarCliente()
                                .contaId(null)
                                .build())
                .withMessage("contaId não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComNomeBlank() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarCliente()
                                .nome(null)
                                .build())
                .withMessage("nome não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarCliente()
                                .nome("")
                                .build())
                .withMessage("nome não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComCpfNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarCliente()
                                .cpf(null)
                                .build())
                .withMessage("cpf não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComTelefoneNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarCliente()
                                .telefone(null)
                                .build())
                .withMessage("telefone não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComEnderecoBlank() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarCliente()
                                .endereco(null)
                                .build())
                .withMessage("endereco não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarCliente()
                                .endereco("")
                                .build())
                .withMessage("endereco não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComDataNascimentoInvalida() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarCliente()
                                .dataNascimento(null)
                                .build())
                .withMessage("data de nascimento inválida, deve possuir ao menos '14' anos.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarCliente()
                                .dataNascimento(LocalDate.now())
                                .build())
                .withMessage("data de nascimento inválida, deve possuir ao menos '14' anos.");
    }

    @Test
    void deveAceitarTemosComVersao() {
        Cliente cliente = criarCliente()
                .aceiteTermos(false)
                .versaoTermos(null)
                .dataAceiteTermos(null)
                .build();
        String versao = "1.0";
        LocalDateTime dataAceite = LocalDateTime.now();

        cliente.aceitarTermos(versao, dataAceite);

        assertThat(cliente.getVersaoTermos()).isEqualTo(versao);
        assertThat(cliente.getDataAceiteTermos()).isEqualTo(dataAceite);
        assertThat(cliente.isAceiteTermos()).isTrue();
    }

    @Test
    void deveRetornarTrue_QuandoTemSaldo() {
        Cliente cliente = criarCliente()
                .saldoPontos(100)
                .build();

        assertThat(cliente.temSaldo(100)).isTrue();
        assertThat(cliente.temSaldo(50)).isTrue();
    }

    @Test
    void deveRetornarFalse_QuandoNaoTemSaldo() {
        Cliente cliente = criarCliente()
                .saldoPontos(100)
                .build();

        assertThat(cliente.temSaldo(200)).isFalse();
    }

    @Test
    void deveCreditarPontos() {
        Cliente cliente = criarCliente()
                .saldoPontos(100)
                .build();

        cliente.creditar(10);

        assertThat(cliente.temSaldo(110)).isTrue();
    }

    @Test
    void deveDebitarPontos() {
        Cliente cliente = criarCliente()
                .saldoPontos(100)
                .build();

        cliente.debitar(10);

        assertThat(cliente.temSaldo(100)).isFalse();
    }

    private Cliente.ClienteBuilder criarCliente() {
        return Cliente.builder()
                .id(Id.aleatorio())
                .contaId(Id.aleatorio())
                .nome("Nome")
                .cpf(new CPF("52998224725"))
                .telefone(new Telefone("11999999999"))
                .endereco("Endereco")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .saldoPontos(0)
                .aceiteTermos(true)
                .dataAceiteTermos(LocalDateTime.now())
                .versaoTermos("1.0")
                .dataCadastro(LocalDateTime.now());
    }
}
