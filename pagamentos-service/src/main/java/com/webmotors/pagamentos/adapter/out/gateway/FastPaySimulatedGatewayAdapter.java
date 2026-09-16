package com.webmotors.pagamentos.adapter.out.gateway;

import com.webmotors.pagamentos.application.port.out.FastPayGatewayPort;
import com.webmotors.pagamentos.domain.model.Payment;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Simula o gateway externo webmotors-fastpay: latencia variavel e falhas transitorias,
 * para exercitar Retry/CircuitBreaker/Bulkhead sob carga sem depender de um provedor real.
 */
@Component
public class FastPaySimulatedGatewayAdapter implements FastPayGatewayPort {
    private static final int FAILURE_RATE_PERCENT = 15;

    @Override
    public void processarPagamento(Payment payment) {
        var random = ThreadLocalRandom.current();
        try {
            Thread.sleep(30 + random.nextInt(150));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Processamento do pagamento interrompido", e);
        }
        if (random.nextInt(100) < FAILURE_RATE_PERCENT) {
            throw new FastPayIndisponivelException("fastpay indisponivel temporariamente para " + payment.idempotencyKey());
        }
    }
}
