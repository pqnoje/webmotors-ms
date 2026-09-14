package com.webmotors.anuncios.adapter.out.persistence;

import com.webmotors.anuncios.application.port.out.ProcessedEventRepositoryPort;
import com.webmotors.anuncios.application.model.ProcessedEvent;
import org.springframework.stereotype.Component;

@Component
public class ProcessedEventPersistenceAdapter implements ProcessedEventRepositoryPort {
    private final ProcessedEventRepository repository;

    public ProcessedEventPersistenceAdapter(ProcessedEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsById(String eventId) {
        return repository.existsById(eventId);
    }

    @Override
    public ProcessedEvent save(ProcessedEvent event) {
        return toDomain(repository.save(toEntity(event)));
    }

    private ProcessedEventJpaEntity toEntity(ProcessedEvent event) {
        return new ProcessedEventJpaEntity(
            event.eventId(),
            event.eventType(),
            event.processedAt()
        );
    }

    private ProcessedEvent toDomain(ProcessedEventJpaEntity entity) {
        return new ProcessedEvent(
            entity.getEventId(),
            entity.getEventType(),
            entity.getProcessedAt()
        );
    }
}