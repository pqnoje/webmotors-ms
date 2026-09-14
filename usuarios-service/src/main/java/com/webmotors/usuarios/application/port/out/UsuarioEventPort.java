package com.webmotors.usuarios.application.port.out;

public interface UsuarioEventPort {
    void publicarUsuarioDesativado(Long usuarioId);
}