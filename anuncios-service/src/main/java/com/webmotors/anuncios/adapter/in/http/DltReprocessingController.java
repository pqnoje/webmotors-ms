package com.webmotors.anuncios.adapter.in.http;

import com.webmotors.anuncios.adapter.in.kafka.DesativacaoPorMarcaBatchConsumer;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anuncios/dlt")
public class DltReprocessingController {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MeterRegistry meterRegistry;

    public DltReprocessingController(
        KafkaTemplate<String, String> kafkaTemplate,
        MeterRegistry meterRegistry
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping("/reprocessar")
    public void reprocessar(@Valid @RequestBody DltReprocessRequest request) {
        kafkaTemplate.send(
            DesativacaoPorMarcaBatchConsumer.TOPIC,
            request.eventId(),
            request.payload()
        );
        meterRegistry.counter("desativacao_dlt_reprocessadas_total").increment();
    }

    public record DltReprocessRequest(
        @NotBlank String eventId,
        @NotBlank String payload
    ) {
    }
}