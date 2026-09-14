package com.webmotors.anuncios.adapter.in.http.dto;

import com.webmotors.anuncios.application.port.in.AnuncioUseCase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarAnuncioRequest(
    @NotNull Long usuarioId,
    @NotBlank String marca,
    @NotBlank String modelo,
    @NotNull Integer ano,
    @NotNull BigDecimal preco,
    String descricao
) {
    public AnuncioUseCase.CriarAnuncioCommand toCommand() {
        return new AnuncioUseCase.CriarAnuncioCommand(
            usuarioId,
            marca,
            modelo,
            ano,
            preco,
            descricao
        );
    }
}