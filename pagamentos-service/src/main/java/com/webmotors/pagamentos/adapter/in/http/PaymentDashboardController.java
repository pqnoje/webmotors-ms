package com.webmotors.pagamentos.adapter.in.http;

import com.webmotors.pagamentos.application.port.out.PaymentRepositoryPort;
import com.webmotors.pagamentos.domain.model.PaymentStatus;
import com.webmotors.pagamentos.generator.PaymentGenerator;
import com.webmotors.pagamentos.metrics.PaymentMetrics;
import jakarta.annotation.PreDestroy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Painel em tempo real: transmite os contadores via Server-Sent Events e expõe um
 * controle (two-way) para ajustar, ao vivo, quantas transacoes por segundo o gerador dispara.
 */
@RestController
@RequestMapping("/pagamentos/dashboard")
public class PaymentDashboardController {
    private final PaymentMetrics metrics;
    private final PaymentGenerator generator;
    private final PaymentRepositoryPort repository;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "dashboard-broadcaster");
        thread.setDaemon(true);
        return thread;
    });

    public PaymentDashboardController(PaymentMetrics metrics, PaymentGenerator generator, PaymentRepositoryPort repository) {
        this.metrics = metrics;
        this.generator = generator;
        this.repository = repository;
        scheduler.scheduleAtFixedRate(this::broadcast, 0, 500, TimeUnit.MILLISECONDS);
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(ex -> emitters.remove(emitter));
        return emitter;
    }

    @GetMapping
    public Map<String, Object> snapshot() {
        return toPayload();
    }

    @PostMapping("/gerador")
    public Map<String, Object> ajustarGerador(@RequestParam int tps) {
        generator.setTransacoesPorSegundo(tps);
        return Map.of("transacoesPorSegundo", generator.getTransacoesPorSegundo());
    }

    private void broadcast() {
        Map<String, Object> payload = toPayload();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("metrics").data(payload));
            } catch (IOException | IllegalStateException ex) {
                emitters.remove(emitter);
            }
        }
    }

    private Map<String, Object> toPayload() {
        var pagamentos = repository.findAll();
        long concluidos = pagamentos.stream()
            .filter(payment -> payment.status() == PaymentStatus.COMPLETED)
            .count();
        long pendentes = pagamentos.stream()
            .filter(payment -> payment.status() == PaymentStatus.PENDING)
            .count();
        var metricas = metrics.snapshot();
        return Map.of(
            "solicitados", metricas.solicitados(),
            "recebidos", pagamentos.size(),
            "concluidos", concluidos,
            "falharam", metricas.falharam(),
            "pendentes", pendentes,
                "transacoesPorSegundo", generator.getTransacoesPorSegundo()
        );
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdownNow();
    }
}
