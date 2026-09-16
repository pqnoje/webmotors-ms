package com.webmotors.pagamentos.adapter.out.gateway;

import com.webmotors.pagamentos.application.port.out.FastPayGatewayPort;
import com.webmotors.pagamentos.domain.model.Payment;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Decorator que adiciona resiliencia (Retry + CircuitBreaker + Bulkhead) em torno do
 * gateway webmotors-fastpay. Precisa ser um bean separado do chamador para que o proxy
 * do Spring AOP intercepte a chamada (self-invocation nao ativa as anotacoes).
 */
@Primary
@Component
public class ResilientFastPayGateway implements FastPayGatewayPort {
    private static final String FASTPAY = "fastpay";

    private final FastPaySimulatedGatewayAdapter delegate;

    public ResilientFastPayGateway(FastPaySimulatedGatewayAdapter delegate) {
        this.delegate = delegate;
    }

    @Retry(name = FASTPAY)
    @CircuitBreaker(name = FASTPAY)
    @Bulkhead(name = FASTPAY)
    @Override
    public void processarPagamento(Payment payment) {
        delegate.processarPagamento(payment);
    }
}
