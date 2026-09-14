package com.webmotors.anuncios.application.port.in;

public interface ProcessarUsuarioDesativadoUseCase {
    void processar(UsuarioDesativadoCommand command);

    record UsuarioDesativadoCommand(
        String eventId,
        String eventType,
        Long usuarioId
    ) {
    }
}