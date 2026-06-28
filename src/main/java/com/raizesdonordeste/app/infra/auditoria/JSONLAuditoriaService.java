package com.raizesdonordeste.app.infra.auditoria;

import com.raizesdonordeste.app.application.services.AuditoriaService;
import com.raizesdonordeste.app.domain.auditoria.model.RegistroAuditoria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class JSONLAuditoriaService implements AuditoriaService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final JsonMapper jsonMapper;

    @Override
    public void registrar(RegistroAuditoria registroAuditoria) {
        Path arquivo = obterArquivoLog();

        try {
            Files.createDirectories(arquivo.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(
                    arquivo,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {

                writer.write(jsonMapper.writeValueAsString(registroAuditoria));
                writer.newLine();
            }
        } catch (IOException e) {
            log.error("Erro ao registrar auditoria.", e);
        }
    }

    private Path obterArquivoLog() {
        String prefixo = "auditoria-";
        String extensao = ".jsonl";
        String nome = prefixo + LocalDate.now().format(formatter) + extensao;
        String diretorio = "logs";
        return Path.of(diretorio, nome);
    }
}
