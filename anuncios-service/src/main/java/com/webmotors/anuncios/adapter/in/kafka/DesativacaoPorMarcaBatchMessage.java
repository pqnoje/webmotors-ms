package com.webmotors.anuncios.adapter.in.kafka;

import java.util.List;

public record DesativacaoPorMarcaBatchMessage(
    String eventId,
    String eventType,
    int eventVersion,
    String occurredAt,
    String marca,
    List<Long> usuarioIds
) {
}