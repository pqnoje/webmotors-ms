package com.webmotors.anuncios.adapter.in.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webmotors.anuncios.application.port.in.ProcessarUsuarioDesativadoUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEventConsumer {
    private final ObjectMapper objectMapper;
    private final ProcessarUsuarioDesativadoUseCase useCase;

    public UsuarioEventConsumer(
        ObjectMapper objectMapper,
        ProcessarUsuarioDesativadoUseCase useCase
    ) {
        this.objectMapper = objectMapper;
        this.useCase = useCase;
    }

    @KafkaListener(topics = "webmotors.usuarios.v1")
    public void consumir(String payload) throws Exception {
        UsuarioDesativadoMessage message = objectMapper.readValue(payload, UsuarioDesativadoMessage.class);
        useCase.processar(
            new ProcessarUsuarioDesativadoUseCase.UsuarioDesativadoCommand(
                message.eventId(),
                message.eventType(),
                message.usuarioId()
            )
        );
    }
}