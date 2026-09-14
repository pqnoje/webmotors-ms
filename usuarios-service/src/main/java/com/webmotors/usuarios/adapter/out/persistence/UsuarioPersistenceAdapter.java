package com.webmotors.usuarios.adapter.out.persistence;

import com.webmotors.usuarios.application.port.out.UsuarioRepositoryPort;
import com.webmotors.usuarios.domain.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {
    private final UsuarioRepository repository;

    public UsuarioPersistenceAdapter(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        return toDomain(repository.save(toEntity(usuario)));
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    private UsuarioJpaEntity toEntity(Usuario usuario) {
        return new UsuarioJpaEntity(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.isAtivo()
        );
    }

    private Usuario toDomain(UsuarioJpaEntity entity) {
        return Usuario.reconstituir(
            entity.getId(),
            entity.getNome(),
            entity.getEmail(),
            entity.isAtivo()
        );
    }
}