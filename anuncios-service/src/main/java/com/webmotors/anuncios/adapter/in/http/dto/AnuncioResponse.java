package com.webmotors.anuncios.adapter.in.http.dto;

import com.webmotors.anuncios.domain.model.Anuncio;

import java.math.BigDecimal;

public record AnuncioResponse(
    Long id,
    Long usuarioId,
    String marca,
    String modelo,
    Integer ano,
    BigDecimal preco,
    String descricao,
    boolean ativo
) {
    public static AnuncioResponse from(Anuncio anuncio) {
        return new AnuncioResponse(
            anuncio.getId(),
            anuncio.getUsuarioId(),
            anuncio.getMarca(),
            anuncio.getModelo(),
            anuncio.getAno(),
            anuncio.getPreco(),
            anuncio.getDescricao(),
            anuncio.isAtivo()
        );
    }
}