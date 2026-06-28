package com.raizesdonordeste.app.application.services;

import com.raizesdonordeste.app.domain.auditoria.model.RegistroAuditoria;

public interface AuditoriaService {

    void registrar(RegistroAuditoria registroAuditoria);
}
