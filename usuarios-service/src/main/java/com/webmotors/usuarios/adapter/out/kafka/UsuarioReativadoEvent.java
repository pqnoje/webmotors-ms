package com.webmotors.usuarios.adapter.out.kafka;

import java.time.Instant;
import java.util.UUID;

public record UsuarioReativadoEvent(
    String eventId,
    String eventType,
    int eventVersion,
    Instant occurredAt,
    Long usuarioId
) implements UsuarioEvent {
    public static UsuarioReativadoEvent of(Long usuarioId) {
        return new UsuarioReativadoEvent(
            UUID.randomUUID().toString(),
            "UsuarioReativado",
            1,
            Instant.now(),
            usuarioId
        );
    }
}