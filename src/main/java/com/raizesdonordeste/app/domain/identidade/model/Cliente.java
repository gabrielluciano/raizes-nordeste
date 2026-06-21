package com.raizesdonordeste.app.domain.identidade.model;

import com.raizesdonordeste.app.domain.comum.model.CPF;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Telefone;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class Cliente {

    private static final int IDADE_MINIMA_CLIENTE = 14;

    private Id id;
    private Id contaId;
    private String nome;
    private CPF cpf;
    private Telefone telefone;
    private String endereco;
    private LocalDate dataNascimento;
    private long saldoPontos;
    private boolean aceiteTermos;
    private LocalDateTime dataAceiteTermos;
    private String versaoTermos;
    private LocalDateTime dataCadastro;

    @Builder
    public Cliente(Id id,
                   Id contaId,
                   String nome,
                   CPF cpf,
                   Telefone telefone,
                   String endereco,
                   LocalDate dataNascimento,
                   long saldoPontos,
                   boolean aceiteTermos,
                   LocalDateTime dataAceiteTermos,
                   String versaoTermos,
                   LocalDateTime dataCadastro
    ) {
        if (!dataNascimentoValida(dataNascimento)) {
            throw new IllegalArgumentException(
                    "data de nascimento inválida, deve possuir ao menos '%s' anos.".formatted(IDADE_MINIMA_CLIENTE));
        }

        this.id = Guarda.naoNulo(id, "id");
        this.contaId = Guarda.naoNulo(contaId, "contaId");
        this.nome = Guarda.naoVazio(nome, "nome");
        this.cpf = Guarda.naoNulo(cpf, "cpf");
        this.telefone = Guarda.naoNulo(telefone, "telefone");
        this.endereco = Guarda.naoVazio(endereco, "endereco");
        this.dataNascimento = dataNascimento;
        this.saldoPontos = saldoPontos;
        this.aceiteTermos = aceiteTermos;
        this.dataAceiteTermos = dataAceiteTermos;
        this.versaoTermos = versaoTermos;
        this.dataCadastro = dataCadastro;
    }

    public static Cliente criar(
            Id contaId,
            String nome,
            String cpf,
            String telefone,
            String endereco,
            LocalDate dataNascimento,
            String versaoTermos
    ) {
        LocalDateTime now = LocalDateTime.now();
        return new Cliente(
                Id.aleatorio(),
                contaId,
                nome,
                new CPF(cpf),
                new Telefone(telefone),
                endereco,
                dataNascimento,
                0,
                true,
                now,
                versaoTermos,
                now
        );
    }

    private boolean dataNascimentoValida(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            return false;
        }

        LocalDate dataLimite = LocalDate.now().minusYears(IDADE_MINIMA_CLIENTE);

        return !dataNascimento.isAfter(dataLimite);
    }

    public void aceitarTermos(String versao, LocalDateTime dataAceite) {
        this.versaoTermos = versao;
        this.dataAceiteTermos = dataAceite;
        this.aceiteTermos = true;
    }

    public boolean temSaldo(long pontos) {
        return this.saldoPontos >= pontos;
    }

    public void creditar(long pontos) {
        this.saldoPontos += pontos;
    }

    public void debitar(long pontos) {
        this.saldoPontos -= pontos;
    }
}
