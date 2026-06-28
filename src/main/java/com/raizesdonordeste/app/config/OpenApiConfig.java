package com.raizesdonordeste.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                version = "0.0.1",
                title = "Raízes do Nordeste Backend",
                description = "API do backend Raízes do Nordeste."
        ),
        security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME)
)
@SecurityScheme(
        name = OpenApiConfig.SECURITY_SCHEME,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Token JWT obtido em /auth/login."
)
public class OpenApiConfig {

    public static final String SECURITY_SCHEME = "bearerAuth";
}
