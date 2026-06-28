package com.raizesdonordeste.app.application.usecases;

import com.raizesdonordeste.app.application.services.AuditoriaService;
import com.raizesdonordeste.app.domain.auditoria.model.AtorTipo;
import com.raizesdonordeste.app.domain.auditoria.model.EventoAuditoria;
import com.raizesdonordeste.app.domain.auditoria.model.RegistroAuditoria;
import com.raizesdonordeste.app.domain.identidade.exceptions.AcessoNegadoException;
import com.raizesdonordeste.app.domain.identidade.exceptions.FuncionarioNaoEncontradoException;
import com.raizesdonordeste.app.domain.identidade.model.Funcionario;
import com.raizesdonordeste.app.domain.identidade.repository.FuncionarioRepository;
import com.raizesdonordeste.app.domain.pedido.exceptions.PedidoNaoEncontradoException;
import com.raizesdonordeste.app.domain.pedido.model.AvancarStatusPedidoComando;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.model.StatusPedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AvancarStatusPedidoUseCase implements CasoDeUso<AvancarStatusPedidoComando, StatusPedido> {

    private final PedidoRepository pedidoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final AuditoriaService auditoriaService;

    @Override
    @Transactional
    public StatusPedido executar(AvancarStatusPedidoComando comando) {
        Funcionario funcionario = funcionarioRepository.obterPorContaId(comando.contaId())
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(
                        "funcionário de contaId '%s' não encontrado.".formatted(comando.contaId())));

        Pedido pedido = pedidoRepository.obterPorId(comando.pedidoId())
                .orElseThrow(() -> new PedidoNaoEncontradoException(comando.pedidoId()));

        if (!Objects.equals(funcionario.unidadeId(), pedido.getUnidadeId())) {
            throw new AcessoNegadoException("funcionário não tem permissão para alterar status do pedido");
        }

        pedido.avancarStatus();
        pedidoRepository.atualizar(pedido);

        auditoriaService.registrar(RegistroAuditoria.criar(
                AtorTipo.FUNCIONARIO,
                funcionario.id().toString(),
                EventoAuditoria.PEDIDO_STATUS_AVANCADO,
                "Pedido",
                pedido.getId().toString(),
                Map.of("novoStatus", pedido.getStatus().name())
        ));

        return pedido.getStatus();
    }
}
