package com.webmotors.usuarios.adapter.out.kafka;

import java.time.Instant;
import java.util.UUID;

public record UsuarioCriadoEvent(
    String eventId,
    String eventType,
    int eventVersion,
    Instant occurredAt,
    Long usuarioId,
    String nome,
    String email
) implements UsuarioEvent {
    public static UsuarioCriadoEvent of(Long usuarioId, String nome, String email) {
        return new UsuarioCriadoEvent(
            UUID.randomUUID().toString(),
            "UsuarioCriado",
            1,
            Instant.now(),
            usuarioId,
            nome,
            email
        );
    }
}