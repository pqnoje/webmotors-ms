package com.webmotors.pagamentos.application.usecase;

import com.webmotors.pagamentos.application.port.out.FastPayGatewayPort;
import com.webmotors.pagamentos.application.port.out.PaymentRepositoryPort;
import com.webmotors.pagamentos.domain.model.Payment;
import com.webmotors.pagamentos.domain.model.PaymentStatus;
import com.webmotors.pagamentos.metrics.PaymentMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Tenta processar o pagamento no webmotors-fastpay. Usado tanto na tentativa imediata
 * (fluxo sincrono do use case) quanto na reconciliacao em background dos pagamentos PENDING,
 * garantindo que o pagamento sempre acaba sendo processado mesmo apos um gargalo momentaneo.
 */
@Component
public class PaymentProcessor {
    private static final Logger log = LoggerFactory.getLogger(PaymentProcessor.class);

    private final PaymentRepositoryPort repository;
    private final FastPayGatewayPort fastPay;
    private final PaymentMetrics metrics;

    public PaymentProcessor(PaymentRepositoryPort repository, FastPayGatewayPort fastPay, PaymentMetrics metrics) {
        this.repository = repository;
        this.fastPay = fastPay;
        this.metrics = metrics;
    }

    public Payment processar(Payment payment) {
        try {
            fastPay.processarPagamento(payment);
            Payment concluido = repository.save(payment.withStatus(PaymentStatus.COMPLETED));
            metrics.registrarConcluido();
            return concluido;
        } catch (Exception ex) {
            metrics.registrarFalha();
            log.warn("Pagamento {} nao processado agora ({}), permanece PENDING para nova tentativa",
                    payment.idempotencyKey(), ex.toString());
            return repository.save(payment.withStatus(PaymentStatus.PENDING));
        }
    }
}
