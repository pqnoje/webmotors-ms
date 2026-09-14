package com.webmotors.usuarios.adapter.in.http.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DesativacaoEmMassaRequest(
    @NotEmpty
    @Size(max = 10_000)
    List<@NotNull @Valid Long> ids
) {
}