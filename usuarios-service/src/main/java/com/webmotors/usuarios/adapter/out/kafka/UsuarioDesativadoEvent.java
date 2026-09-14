package com.webmotors.usuarios.adapter.out.kafka;

import java.time.Instant;

public record UsuarioDesativadoEvent(
    String eventId,
    String eventType,
    int eventVersion,
    Instant occurredAt,
    Long usuarioId
) implements UsuarioEvent {
    public static UsuarioDesativadoEvent of(Long usuarioId) {
        return new UsuarioDesativadoEvent(
            java.util.UUID.randomUUID().toString(),
            "UsuarioDesativado",
            1,
            Instant.now(),
            usuarioId
        );
    }
}