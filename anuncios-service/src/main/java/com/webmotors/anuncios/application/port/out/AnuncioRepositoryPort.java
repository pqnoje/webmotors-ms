package com.webmotors.anuncios.application.port.out;

import com.webmotors.anuncios.domain.model.Anuncio;

import java.util.List;

public interface AnuncioRepositoryPort {
    Anuncio save(Anuncio anuncio);

    List<Anuncio> findAll();

    List<Anuncio> findByUsuarioId(Long usuarioId);

    List<Long> findUsuarioIdsPorMarca(String marca);

    int desativarPorUsuarioId(Long usuarioId);

    int desativarPorUsuarioIds(List<Long> usuarioIds);
}