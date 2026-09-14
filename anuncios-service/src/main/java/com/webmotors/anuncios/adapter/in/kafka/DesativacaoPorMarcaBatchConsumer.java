package com.webmotors.anuncios.adapter.in.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webmotors.anuncios.application.model.ProcessedEvent;
import com.webmotors.anuncios.application.port.in.AnuncioUseCase;
import com.webmotors.anuncios.application.port.out.ProcessedEventRepositoryPort;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class DesativacaoPorMarcaBatchConsumer {
    public static final String TOPIC = "webmotors.usuarios.desativacao.v1";

    private final ObjectMapper objectMapper;
    private final AnuncioUseCase anuncioUseCase;
    private final ProcessedEventRepositoryPort processedEventRepository;
    private final MeterRegistry meterRegistry;

    public DesativacaoPorMarcaBatchConsumer(
        ObjectMapper objectMapper,
        AnuncioUseCase anuncioUseCase,
        ProcessedEventRepositoryPort processedEventRepository,
        MeterRegistry meterRegistry
    ) {
        this.objectMapper = objectMapper;
        this.anuncioUseCase = anuncioUseCase;
        this.processedEventRepository = processedEventRepository;
        this.meterRegistry = meterRegistry;
    }

    @KafkaListener(
        topics = TOPIC,
        groupId = "anuncios-desativacao-por-marca",
        containerFactory = "batchKafkaListenerContainerFactory"
    )
    @Transactional
    public void consumir(String payload) throws Exception {
        DesativacaoPorMarcaBatchMessage message = objectMapper.readValue(
            payload,
            DesativacaoPorMarcaBatchMessage.class
        );
        if (processedEventRepository.existsById(message.eventId())) {
            return;
        }

        int anunciosDesativados = anuncioUseCase.desativarPorUsuarioIds(message.usuarioIds());
        processedEventRepository.save(
            new ProcessedEvent(message.eventId(), message.eventType(), Instant.now())
        );
        meterRegistry.counter("desativacao_lotes_processados_total").increment();
        meterRegistry.counter("desativacao_usuarios_processados_total")
            .increment(message.usuarioIds().size());
        meterRegistry.counter("desativacao_anuncios_processados_total")
            .increment(anunciosDesativados);
    }
}