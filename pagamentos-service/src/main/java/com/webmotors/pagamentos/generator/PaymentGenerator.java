package com.webmotors.pagamentos.generator;

import com.webmotors.pagamentos.application.port.in.PaymentUseCase;
import com.webmotors.pagamentos.domain.model.PaymentMethod;
import com.webmotors.pagamentos.metrics.PaymentMetrics;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Gera pagamentos PIX/BOLETO/TED automaticamente na mesma maquina, para simular
 * carga continua e testar o comportamento do circuit breaker/bulkhead sob pico de CPU.
 * A taxa (transacoes por segundo) e ajustavel em tempo real via /pagamentos/dashboard/gerador.
 */
@Component
public class PaymentGenerator {
    private static final PaymentMethod[] METODOS = PaymentMethod.values();

    private final PaymentUseCase useCase;
    private final PaymentMetrics metrics;
    private final int maxTps;
    private final ExecutorService workers;
    private final AtomicInteger transacoesPorSegundo = new AtomicInteger(0);

    public PaymentGenerator(
            PaymentUseCase useCase,
            PaymentMetrics metrics,
            @Value("${fastpay.generator.max-tps:2000}") int maxTps,
            @Value("${fastpay.generator.worker-count:32}") int workerCount,
            @Value("${fastpay.generator.queue-capacity:1000}") int queueCapacity) {
        this.useCase = useCase;
        this.metrics = metrics;
        this.maxTps = Math.max(1, maxTps);
        int workersCount = Math.max(1, workerCount);
        int capacity = Math.max(1, queueCapacity);
        this.workers = new ThreadPoolExecutor(
                workersCount,
                workersCount,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(capacity),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void setTransacoesPorSegundo(int tps) {
        transacoesPorSegundo.set(Math.max(0, Math.min(tps, maxTps)));
    }

    public int getTransacoesPorSegundo() {
        return transacoesPorSegundo.get();
    }

    @Scheduled(fixedRate = 200)
    public void gerar() {
        int tps = transacoesPorSegundo.get();
        if (tps <= 0) {
            return;
        }
        int porCiclo = Math.max(1, Math.round(tps / 5f));
        for (int i = 0; i < porCiclo; i++) {
            metrics.registrarSolicitado();
            workers.execute(this::gerarPagamento);
        }
    }

    private void gerarPagamento() {
        var random = ThreadLocalRandom.current();
        long usuarioId = 1 + random.nextInt(5);
        BigDecimal amount = BigDecimal.valueOf(10 + random.nextInt(5000))
                .setScale(2, java.math.RoundingMode.HALF_UP);
        PaymentMethod method = METODOS[random.nextInt(METODOS.length)];
        useCase.create(usuarioId, amount, method, "auto-" + UUID.randomUUID());
    }

    @PreDestroy
    public void shutdown() {
        workers.shutdownNow();
    }
}
