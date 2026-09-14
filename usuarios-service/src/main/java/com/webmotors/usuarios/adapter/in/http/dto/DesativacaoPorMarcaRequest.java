package com.webmotors.usuarios.adapter.in.http.dto;

import jakarta.validation.constraints.NotBlank;

public record DesativacaoPorMarcaRequest(
    @NotBlank String marca
) {
}