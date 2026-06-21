package com.raizesdonordeste.app.domain.identidade.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class RefreshToken {

    private Id id;
    private Id contaId;
    private String tokenHash;
    private LocalDateTime expiraEm;
    private LocalDateTime revogadoEm;

    @Builder
    public RefreshToken(Id id, Id contaId, String tokenHash, LocalDateTime expiraEm, LocalDateTime revogadoEm) {
        this.id = Guarda.naoNulo(id, "id");
        this.contaId = Guarda.naoNulo(contaId, "contaId");
        this.tokenHash = Guarda.naoVazio(tokenHash, "tokenHash");
        this.expiraEm = Guarda.naoNulo(expiraEm, "expiraEm");
        this.revogadoEm = revogadoEm;
    }

    public static RefreshToken criar(Id contaId, String tokenHash, Duration duracao) {
        Guarda.naoNulo(duracao, "duracao");
        return new RefreshToken(
                Id.aleatorio(),
                contaId,
                tokenHash,
                LocalDateTime.now().plus(duracao),
                null
        );
    }

    public void revogar() {
        if (revogadoEm == null) {
            revogadoEm = LocalDateTime.now();
        }
    }

    public boolean valido() {
        return revogadoEm == null && LocalDateTime.now().isBefore(expiraEm);
    }
}
