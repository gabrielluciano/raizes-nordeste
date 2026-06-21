package com.raizesdonordeste.app.infra.crypto;

import com.raizesdonordeste.app.domain.identidade.services.SenhaHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SenhaHasherImpl implements SenhaHasher {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String gerarHash(String senha) {
        return passwordEncoder.encode(senha);
    }

    @Override
    public boolean verificarSenha(String senha, String senhaHash) {
        return passwordEncoder.matches(senha, senhaHash);
    }
}
