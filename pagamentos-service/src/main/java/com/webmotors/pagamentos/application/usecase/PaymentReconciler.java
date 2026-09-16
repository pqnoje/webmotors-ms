package com.webmotors.pagamentos.application.usecase;

import com.webmotors.pagamentos.application.port.out.PaymentRepositoryPort;
import com.webmotors.pagamentos.domain.model.PaymentStatus;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Reconcilia pagamentos que ficaram PENDING por causa de um gargalo momentaneo
 * (circuit aberto, bulkhead cheio, falha transitoria) e tenta processa-los de novo,
 * garantindo que o pagamento eventualmente conclui mesmo apos o pico de carga passar.
 */
@Component
public class PaymentReconciler {
    private final PaymentRepositoryPort repository;
    private final PaymentProcessor processor;
    private final int batchSize;
    private final ExecutorService workers;

    public PaymentReconciler(
            PaymentRepositoryPort repository,
            PaymentProcessor processor,
            @Value("${fastpay.reconciler.worker-count:8}") int workerCount,
            @Value("${fastpay.reconciler.batch-size:100}") int batchSize) {
        this.repository = repository;
        this.processor = processor;
        this.batchSize = Math.max(1, batchSize);
        this.workers = Executors.newFixedThreadPool(Math.max(1, workerCount));
    }

    @Scheduled(fixedDelayString = "${fastpay.reconciler.interval-ms:2000}")
    public void reprocessarPendentes() {
        var pendentes = repository.findByStatus(PaymentStatus.PENDING).stream()
                .limit(batchSize)
                .toList();
        if (pendentes.isEmpty()) {
            return;
        }

        try {
            workers.invokeAll(pendentes.stream()
                    .<java.util.concurrent.Callable<Void>>map(payment -> () -> {
                        processor.processar(payment);
                        return null;
                    })
                    .toList());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @PreDestroy
    public void shutdown() {
        workers.shutdownNow();
    }
}
