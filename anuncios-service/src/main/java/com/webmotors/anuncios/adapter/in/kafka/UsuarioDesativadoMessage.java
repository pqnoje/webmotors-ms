package com.webmotors.anuncios.adapter.in.kafka;

import java.time.Instant;

public record UsuarioDesativadoMessage(
    String eventId,
    String eventType,
    int eventVersion,
    Instant occurredAt,
    Long usuarioId
) {
}