package com.raizesdonordeste.app.domain.organizacao.model;

import com.raizesdonordeste.app.domain.comum.model.Horario;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Unidade {

    private final Id id;
    private final String nome;
    private final String endereco;
    private final Horario horarioFuncionamento;
    private final boolean ativa;

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
