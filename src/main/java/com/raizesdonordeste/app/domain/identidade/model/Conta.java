package com.raizesdonordeste.app.domain.identidade.model;

import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.comum.model.Email;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import com.raizesdonordeste.app.domain.identidade.services.SenhaHasher;
import com.raizesdonordeste.app.domain.identidade.services.SenhaValidator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Conta {

    private final Id id;
    private final Email email;
    private String senhaHash;
    private StatusConta status;
    private final Role role;

    @Builder
    public Conta(Id id, Email email, String senhaHash, StatusConta status, Role role) {
        this.id = Guarda.naoNulo(id, "id");
        this.email = Guarda.naoNulo(email, "email");
        this.senhaHash = Guarda.naoVazio(senhaHash, "senhaHash");
        this.status = Guarda.naoNulo(status, "status");
        this.role = Guarda.naoNulo(role, "role");
    }

    public static Conta criar(String email, String senha, Role role, SenhaHasher hasher) {
        String senhaHash = hasher.gerarHash(senha);
        return new Conta(Id.aleatorio(), new Email(email), senhaHash, StatusConta.ATIVA, role);
    }

    public boolean verificarSenha(String senha, SenhaHasher hasher) {
        return hasher.verificarSenha(senha, senhaHash);
    }

    public void trocarSenha(String novaSenha, SenhaHasher hasher) {
        if (!SenhaValidator.validaSenha(novaSenha)) {
            throw new ValidacaoException("senha fraca recebida");
        }
        this.senhaHash = hasher.gerarHash(novaSenha);
    }

    public void desativar() {
        this.status = StatusConta.DESATIVADA;
    }

    public boolean temRole(Role role) {
        return this.role == role;
    }
}
