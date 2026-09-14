package com.webmotors.usuarios.application.usecase;

import com.webmotors.usuarios.application.port.in.UsuarioUseCase;
import com.webmotors.usuarios.domain.model.Usuario;
import com.webmotors.usuarios.application.port.out.UsuarioEventPort;
import com.webmotors.usuarios.application.port.out.AnuncioQueryPort;
import com.webmotors.usuarios.application.port.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class UsuarioApplicationService implements UsuarioUseCase {
    private final UsuarioRepositoryPort repository;
    private final UsuarioEventPort eventPublisher;
    private final AnuncioQueryPort anuncioQuery;
    private final ExecutorService desativacaoExecutor;
    private final UsuarioBatchDeactivationService batchDeactivationService;

    public UsuarioApplicationService(
        UsuarioRepositoryPort repository,
        UsuarioEventPort eventPublisher,
        AnuncioQueryPort anuncioQuery,
        ExecutorService desativacaoExecutor,
        UsuarioBatchDeactivationService batchDeactivationService
    ) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.anuncioQuery = anuncioQuery;
        this.desativacaoExecutor = desativacaoExecutor;
        this.batchDeactivationService = batchDeactivationService;
    }

    @Override
    public Usuario criar(UsuarioUseCase.CriarUsuarioCommand command) {
        return repository.save(Usuario.criar(command.nome(), command.email()));
    }

    @Override
    public List<Usuario> listar() {
        return repository.findAll();
    }

    @Override
    public Usuario desativar(Long id) {
        Usuario usuario = repository.findById(Objects.requireNonNull(id))
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado: " + id));
        if (!usuario.isAtivo()) {
            return usuario;
        }
        usuario.desativar();
        Usuario usuarioDesativado = repository.save(usuario);
        eventPublisher.publicarUsuarioDesativado(usuarioDesativado.getId());
        return usuarioDesativado;
    }

    @Override
    public UsuarioUseCase.DesativacaoEmMassaResult desativarEmMassa(List<Long> ids) {
        List<CompletableFuture<UsuarioUseCase.DesativacaoEmMassaResult.Falha>> tarefas = ids.stream()
            .map(id -> CompletableFuture.supplyAsync(() -> desativarComResultado(id), desativacaoExecutor))
            .toList();

        List<UsuarioUseCase.DesativacaoEmMassaResult.Falha> falhas = tarefas.stream()
            .map(tarefa -> tarefa.join())
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(ArrayList::new));

        return new UsuarioUseCase.DesativacaoEmMassaResult(
            ids.size(),
            ids.size() - falhas.size(),
            falhas
        );
    }

    @Override
    public UsuarioUseCase.DesativacaoEmMassaResult desativarPorMarcaProibida(String marca) {
        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException("Marca proibida deve ser informada");
        }

        Set<Long> ids = new LinkedHashSet<>(anuncioQuery.findUsuarioIdsPorMarca(marca.trim()));
        List<UsuarioUseCase.DesativacaoEmMassaResult.Falha> falhas = new ArrayList<>();
        int processados = 0;

        List<CompletableFuture<UsuarioUseCase.DesativacaoEmMassaResult>> tarefas = new ArrayList<>();
        for (int inicio = 0; inicio < ids.size(); inicio += 1_000) {
            List<Long> lote = ids.stream().skip(inicio).limit(1_000).toList();
            tarefas.add(CompletableFuture.supplyAsync(
                () -> batchDeactivationService.desativarLote(marca.trim(), lote),
                desativacaoExecutor
            ));
        }

        for (CompletableFuture<UsuarioUseCase.DesativacaoEmMassaResult> tarefa : tarefas) {
            UsuarioUseCase.DesativacaoEmMassaResult resultado = tarefa.join();
            processados += resultado.processados();
            falhas.addAll(resultado.falhas());
        }

        return new UsuarioUseCase.DesativacaoEmMassaResult(ids.size(), processados, falhas);
    }

    private UsuarioUseCase.DesativacaoEmMassaResult.Falha desativarComResultado(Long id) {
        try {
            desativar(id);
            return null;
        } catch (RuntimeException exception) {
            return new UsuarioUseCase.DesativacaoEmMassaResult.Falha(id, exception.getMessage());
        }
    }
}
