package com.webmotors.usuarios.adapter.in.http.dto;

import com.webmotors.usuarios.application.port.in.UsuarioUseCase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CriarUsuarioRequest(
    @NotBlank String nome,
    @NotBlank @Email String email
) {
    public UsuarioUseCase.CriarUsuarioCommand toCommand() {
        return new UsuarioUseCase.CriarUsuarioCommand(nome, email);
    }
}