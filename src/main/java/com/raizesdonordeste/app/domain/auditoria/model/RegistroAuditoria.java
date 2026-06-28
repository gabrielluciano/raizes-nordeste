package com.raizesdonordeste.app.domain.auditoria.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record RegistroAuditoria(
        String id,
        AtorTipo atorTipo,
        String atorId,
        EventoAuditoria evento,
        String entidade,
        String entidadeId,
        Map<String, String> dados,
        LocalDateTime timestamp
) {

    public static RegistroAuditoria criar(
            AtorTipo atorTipo,
            String atorId,
            EventoAuditoria evento,
            String entidade,
            String entidadeId,
            Map<String, String> dados
    ) {
        return new RegistroAuditoria(
                UUID.randomUUID().toString(),
                atorTipo,
                atorId,
                evento,
                entidade,
                entidadeId,
                dados,
                LocalDateTime.now()
        );
    }
}
