package com.webmotors.anuncios.application.usecase;

import com.webmotors.anuncios.application.port.in.AnuncioUseCase;
import com.webmotors.anuncios.application.port.in.ProcessarUsuarioDesativadoUseCase;
import com.webmotors.anuncios.application.port.out.ProcessedEventRepositoryPort;
import com.webmotors.anuncios.application.model.ProcessedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ProcessarUsuarioDesativadoService implements ProcessarUsuarioDesativadoUseCase {
    private final AnuncioUseCase anuncioUseCase;
    private final ProcessedEventRepositoryPort processedEventRepository;

    public ProcessarUsuarioDesativadoService(
        AnuncioUseCase anuncioUseCase,
        ProcessedEventRepositoryPort processedEventRepository
    ) {
        this.anuncioUseCase = anuncioUseCase;
        this.processedEventRepository = processedEventRepository;
    }

    @Override
    @Transactional
    public void processar(UsuarioDesativadoCommand command) {
        if (processedEventRepository.existsById(command.eventId())) {
            return;
        }

        anuncioUseCase.desativarPorUsuarioId(command.usuarioId());
        processedEventRepository.save(
            new ProcessedEvent(command.eventId(), command.eventType(), Instant.now())
        );
    }
}