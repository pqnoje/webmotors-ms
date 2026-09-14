package com.webmotors.anuncios.application.port.in;

import com.webmotors.anuncios.domain.model.Anuncio;

import java.math.BigDecimal;

import java.util.List;

public interface AnuncioUseCase {
    Anuncio criar(CriarAnuncioCommand command);

    List<Anuncio> listar();

    List<Anuncio> listarPorUsuario(Long usuarioId);

    List<Long> listarUsuarioIdsPorMarca(String marca);

    int desativarPorUsuarioId(Long usuarioId);

    int desativarPorUsuarioIds(List<Long> usuarioIds);

    record CriarAnuncioCommand(
        Long usuarioId,
        String marca,
        String modelo,
        Integer ano,
        BigDecimal preco,
        String descricao
    ) {
    }
}