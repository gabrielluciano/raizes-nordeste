package com.raizesdonordeste.app.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {

    private static final int SALT_LENGTH = 16; // bytes
    private static final int HASH_LENGTH = 32; // bytes

    private static final int PARALLELISM = 1;

    private static final int MEMORY = 65536; // KB (64 MB)
    private static final int ITERATIONS = 3;

    private static final String JWT_ALGORITHM = "HmacSHA256";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(
                SALT_LENGTH,
                HASH_LENGTH,
                PARALLELISM,
                MEMORY,
                ITERATIONS
        );
    }

    @Bean
    public JwtEncoder jwtEncoder(@Value("${app.jwt.secret}") String secret) {
        SecretKeySpec chave = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), JWT_ALGORITHM);
        return new NimbusJwtEncoder(new ImmutableSecret<>(chave));
    }
}
