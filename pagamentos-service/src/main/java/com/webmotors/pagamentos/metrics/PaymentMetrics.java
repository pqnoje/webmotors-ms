package com.webmotors.pagamentos.metrics;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/** Contadores em memoria usados pelo painel em tempo real. */
@Component
public class PaymentMetrics {
    private final AtomicLong solicitados = new AtomicLong();
    private final AtomicLong recebidos = new AtomicLong();
        public void registrarSolicitado() {
            solicitados.incrementAndGet();
        }

    private final AtomicLong concluidos = new AtomicLong();
    private final AtomicLong falharam = new AtomicLong();

    public void registrarRecebido() {
        recebidos.incrementAndGet();
    }

    public void registrarConcluido() {
        concluidos.incrementAndGet();
    }

    public void registrarFalha() {
        falharam.incrementAndGet();
    }

    public Snapshot snapshot() {
        return new Snapshot(solicitados.get(), recebidos.get(), concluidos.get(), falharam.get());
    }

    public record Snapshot(long solicitados, long recebidos, long concluidos, long falharam) {
    }
}
