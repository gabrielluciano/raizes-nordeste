package com.raizesdonordeste.app.infra.mapper;

import com.raizesdonordeste.app.api.dto.PratoResponse;
import com.raizesdonordeste.app.domain.cardapio.model.PratoVisualizacao;
import com.raizesdonordeste.app.domain.comum.model.Id;
import org.springframework.stereotype.Component;

@Component
public class PratoResponseMapper {

    public PratoResponse toResponse(PratoVisualizacao prato) {
        return new PratoResponse(
                idToString(prato.id()),
                idToString(prato.unidadeId()),
                prato.nome(),
                prato.descricao(),
                prato.preco().centavos()
        );
    }

    private String idToString(Id id) {
        return id == null ? null : id.toString();
    }
}
