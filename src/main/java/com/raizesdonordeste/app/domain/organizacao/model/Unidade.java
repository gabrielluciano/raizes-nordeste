package com.raizesdonordeste.app.domain.organizacao.model;

import com.raizesdonordeste.app.domain.comum.model.Horario;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import lombok.Builder;

import java.time.LocalDateTime;

public class Unidade {

    private Id id;
    private String nome;
    private String endereco;
    private Horario horarioFuncionamento;
    private boolean ativa;

    @Builder
    public Unidade(Id id, String nome, String endereco, Horario horarioFuncionamento, boolean ativa) {
        this.id = Guarda.naoNulo(id, "id");
        this.nome = Guarda.naoVazio(nome, "nome");
        this.endereco = Guarda.naoVazio(endereco, "endereco");
        this.horarioFuncionamento = Guarda.naoNulo(horarioFuncionamento, "horarioFuncionamento");
        this.ativa = ativa;
    }

    public boolean estaAberta(LocalDateTime agora) {
        return horarioFuncionamento.estaDentro(agora);
    }
}
