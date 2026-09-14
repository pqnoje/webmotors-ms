package com.webmotors.usuarios.adapter.out.kafka;

import java.time.Instant;
import java.util.List;

public record DesativacaoPorMarcaBatchEvent(
    String eventId,
    String eventType,
    int eventVersion,
    Instant occurredAt,
    String marca,
    List<Long> usuarioIds
) {
    public static DesativacaoPorMarcaBatchEvent of(String marca, List<Long> usuarioIds) {
        return new DesativacaoPorMarcaBatchEvent(
            java.util.UUID.randomUUID().toString(),
            "AnunciosDesativacaoPorMarcaSolicitada",
            1,
            Instant.now(),
            marca,
            List.copyOf(usuarioIds)
        );
    }
}