package com.webmotors.usuarios.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webmotors.usuarios.adapter.out.kafka.DesativacaoPorMarcaBatchEvent;
import com.webmotors.usuarios.adapter.out.persistence.OutboxEventJpaEntity;
import com.webmotors.usuarios.adapter.out.persistence.OutboxEventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UsuarioOutboxService {
    public static final String TOPIC = "webmotors.usuarios.desativacao.v1";

    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MeterRegistry meterRegistry;
    private final AtomicInteger pendingGauge = new AtomicInteger();

    public UsuarioOutboxService(
        OutboxEventRepository repository,
        ObjectMapper objectMapper,
        KafkaTemplate<String, String> kafkaTemplate,
        MeterRegistry meterRegistry
    ) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.meterRegistry = meterRegistry;
        meterRegistry.gauge("desativacao_outbox_pendentes", pendingGauge);
    }

    @Transactional
    public void registrarDesativacaoPorMarca(String marca, List<Long> usuarioIds) {
        DesativacaoPorMarcaBatchEvent event = DesativacaoPorMarcaBatchEvent.of(marca, usuarioIds);
        try {
            repository.save(new OutboxEventJpaEntity(
                event.eventId(),
                TOPIC,
                "lote-" + event.eventId(),
                objectMapper.writeValueAsString(event)
            ));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Nao foi possivel serializar lote da outbox", exception);
        }
    }

    @Scheduled(fixedDelayString = "${outbox.publisher.delay-ms:1000}")
    @Transactional
    public void publicarPendentes() {
        pendingGauge.set(Math.toIntExact(repository.countByStatus("PENDING")));
        List<OutboxEventJpaEntity> pendentes = repository.findByStatusOrderByIdAsc(
            "PENDING",
            PageRequest.of(0, 20)
        );
        for (OutboxEventJpaEntity evento : pendentes) {
            try {
                kafkaTemplate.send(
                    Objects.requireNonNull(evento.getTopic()),
                    Objects.requireNonNull(evento.getEventKey()),
                    Objects.requireNonNull(evento.getPayload())
                ).get();
                evento.markPublished();
                meterRegistry.counter("desativacao_outbox_publicados_total").increment();
            } catch (Exception exception) {
                evento.markFailed(exception);
                meterRegistry.counter("desativacao_outbox_falhos_total").increment();
            }
            repository.save(evento);
        }
    }
}