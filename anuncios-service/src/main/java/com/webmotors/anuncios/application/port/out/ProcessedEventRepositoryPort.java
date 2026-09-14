package com.webmotors.anuncios.application.port.out;

import com.webmotors.anuncios.application.model.ProcessedEvent;

public interface ProcessedEventRepositoryPort {
    boolean existsById(String eventId);

    ProcessedEvent save(ProcessedEvent event);
}