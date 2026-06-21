package com.raizesdonordeste.app.api.resources;

import com.raizesdonordeste.app.api.dto.CriarPedidoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/pedidos")
public class PedidoResource {

    @PostMapping
    public ResponseEntity<Void> inserirPedido(@RequestBody CriarPedidoRequest request) {
        return ResponseEntity.ok().build();
    }
}
