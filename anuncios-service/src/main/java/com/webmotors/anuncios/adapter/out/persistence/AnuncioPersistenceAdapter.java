package com.webmotors.anuncios.adapter.out.persistence;

import com.webmotors.anuncios.application.port.out.AnuncioRepositoryPort;
import com.webmotors.anuncios.domain.model.Anuncio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnuncioPersistenceAdapter implements AnuncioRepositoryPort {
    private final AnuncioRepository repository;

    public AnuncioPersistenceAdapter(AnuncioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Anuncio save(Anuncio anuncio) {
        return toDomain(repository.save(toEntity(anuncio)));
    }

    @Override
    public List<Anuncio> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Anuncio> findByUsuarioId(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Long> findUsuarioIdsPorMarca(String marca) {
        return repository.findDistinctUsuarioIdsByMarca(marca);
    }

    @Override
    public int desativarPorUsuarioId(Long usuarioId) {
        return repository.desativarPorUsuarioId(usuarioId);
    }

    @Override
    public int desativarPorUsuarioIds(List<Long> usuarioIds) {
        return repository.desativarPorUsuarioIds(usuarioIds);
    }

    private AnuncioJpaEntity toEntity(Anuncio anuncio) {
        return new AnuncioJpaEntity(
            anuncio.getId(),
            anuncio.getUsuarioId(),
            anuncio.getMarca(),
            anuncio.getModelo(),
            anuncio.getAno(),
            anuncio.getPreco(),
            anuncio.getDescricao(),
            anuncio.isAtivo(),
            anuncio.getMotivoDesativacao()
        );
    }

    private Anuncio toDomain(AnuncioJpaEntity entity) {
        return Anuncio.reconstituir(
            entity.getId(),
            entity.getUsuarioId(),
            entity.getMarca(),
            entity.getModelo(),
            entity.getAno(),
            entity.getPreco(),
            entity.getDescricao(),
            entity.isAtivo(),
            entity.getMotivoDesativacao()
        );
    }
}