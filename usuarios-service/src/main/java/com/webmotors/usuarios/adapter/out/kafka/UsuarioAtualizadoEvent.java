package com.webmotors.usuarios.adapter.out.kafka;

import java.time.Instant;
import java.util.UUID;

public record UsuarioAtualizadoEvent(
    String eventId,
    String eventType,
    int eventVersion,
    Instant occurredAt,
    Long usuarioId,
    String nome,
    String email
) implements UsuarioEvent {
    public static UsuarioAtualizadoEvent of(Long usuarioId, String nome, String email) {
        return new UsuarioAtualizadoEvent(
            UUID.randomUUID().toString(),
            "UsuarioAtualizado",
            1,
            Instant.now(),
            usuarioId,
            nome,
            email
        );
    }
}