package com.webmotors.anuncios.application.model;

import java.time.Instant;

public record ProcessedEvent(String eventId, String eventType, Instant processedAt) {
}