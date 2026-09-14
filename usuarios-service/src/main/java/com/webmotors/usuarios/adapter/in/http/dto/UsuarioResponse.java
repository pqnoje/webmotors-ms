package com.webmotors.usuarios.adapter.in.http.dto;

import com.webmotors.usuarios.domain.model.Usuario;

public record UsuarioResponse(
    Long id,
    String nome,
    String email,
    boolean ativo
) {
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.isAtivo()
        );
    }
}