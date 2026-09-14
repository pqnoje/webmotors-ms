package com.webmotors.anuncios.application.usecase;

import com.webmotors.anuncios.application.port.in.AnuncioUseCase;
import com.webmotors.anuncios.application.port.out.AnuncioRepositoryPort;
import com.webmotors.anuncios.domain.model.Anuncio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnuncioApplicationService implements AnuncioUseCase {
    private final AnuncioRepositoryPort repository;

    public AnuncioApplicationService(AnuncioRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Anuncio criar(CriarAnuncioCommand command) {
        Anuncio anuncio = Anuncio.criar(
            command.usuarioId(),
            command.marca(),
            command.modelo(),
            command.ano(),
            command.preco(),
            command.descricao()
        );
        return repository.save(anuncio);
    }

    @Override
    public List<Anuncio> listar() {
        return repository.findAll();
    }

    @Override
    public List<Anuncio> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Long> listarUsuarioIdsPorMarca(String marca) {
        return repository.findUsuarioIdsPorMarca(marca);
    }

    @Override
    @Transactional
    public int desativarPorUsuarioId(Long usuarioId) {
        return repository.desativarPorUsuarioId(usuarioId);
    }

    @Override
    @Transactional
    public int desativarPorUsuarioIds(List<Long> usuarioIds) {
        return repository.desativarPorUsuarioIds(usuarioIds);
    }
}