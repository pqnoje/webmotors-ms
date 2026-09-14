package com.webmotors.usuarios.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webmotors.usuarios.application.port.out.UsuarioEventPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UsuarioEventPublisher implements UsuarioEventPort {
    public static final String USUARIOS_TOPIC = "webmotors.usuarios.v1";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public UsuarioEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publicarUsuarioDesativado(Long usuarioId) {
        publicar(UsuarioDesativadoEvent.of(usuarioId));
    }

    public void publicarUsuarioCriado(UsuarioCriadoEvent event) {
        publicar(event);
    }

    public void publicarUsuarioAtualizado(UsuarioAtualizadoEvent event) {
        publicar(event);
    }

    public void publicarUsuarioReativado(UsuarioReativadoEvent event) {
        publicar(event);
    }

    private void publicar(UsuarioEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(
                USUARIOS_TOPIC,
                Objects.requireNonNull(event.usuarioId()).toString(),
                Objects.requireNonNull(payload)
            );
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Nao foi possivel serializar evento de usuario", exception);
        }
    }
}