package com.webmotors.usuarios.application.usecase;

import com.webmotors.usuarios.application.port.in.UsuarioUseCase;
import com.webmotors.usuarios.application.port.out.UsuarioRepositoryPort;
import com.webmotors.usuarios.domain.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioBatchDeactivationService {
    private final UsuarioRepositoryPort repository;
    private final UsuarioOutboxService outboxService;

    public UsuarioBatchDeactivationService(
        UsuarioRepositoryPort repository,
        UsuarioOutboxService outboxService
    ) {
        this.repository = repository;
        this.outboxService = outboxService;
    }

    @Transactional
    public UsuarioUseCase.DesativacaoEmMassaResult desativarLote(String marca, List<Long> ids) {
        List<UsuarioUseCase.DesativacaoEmMassaResult.Falha> falhas = new ArrayList<>();
        List<Long> processados = new ArrayList<>();

        for (Long id : ids) {
            try {
                Usuario usuario = repository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado: " + id));
                if (usuario.isAtivo()) {
                    usuario.desativar();
                    repository.save(usuario);
                }
                processados.add(id);
            } catch (RuntimeException exception) {
                falhas.add(new UsuarioUseCase.DesativacaoEmMassaResult.Falha(id, exception.getMessage()));
            }
        }

        if (!processados.isEmpty()) {
            outboxService.registrarDesativacaoPorMarca(marca, processados);
        }

        return new UsuarioUseCase.DesativacaoEmMassaResult(
            ids.size(),
            processados.size(),
            falhas
        );
    }
}