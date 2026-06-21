package com.raizesdonordeste.app.infra.auth;

import com.raizesdonordeste.app.domain.identidade.model.Conta;
import com.raizesdonordeste.app.domain.identidade.services.ProvedorToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;

@Component
@RequiredArgsConstructor
public class JwtProvedorToken implements ProvedorToken {

    private static final int TAMANHO_REFRESH_BYTES = 32;
    private static final String ROLE_CLAIM = "role";

    private final JwtEncoder jwtEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.jwt.issuer}")
    private String issuer;

    @Override
    public String gerarAccessToken(Conta conta, int duracaoEmMinutos) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(duracaoEmMinutos, ChronoUnit.MINUTES))
                .subject(conta.getId().toString())
                .claim(ROLE_CLAIM, conta.getRole().name())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    @Override
    public String gerarRefreshToken() {
        byte[] bytes = new byte[TAMANHO_REFRESH_BYTES];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Override
    public String hashRefreshToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 indisponível.", e);
        }
    }
}
