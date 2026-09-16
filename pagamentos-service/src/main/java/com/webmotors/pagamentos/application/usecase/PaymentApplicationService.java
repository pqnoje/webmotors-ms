package com.webmotors.pagamentos.application.usecase;

import com.webmotors.pagamentos.application.port.in.PaymentUseCase;
import com.webmotors.pagamentos.application.port.out.PaymentRepositoryPort;
import com.webmotors.pagamentos.domain.model.Payment;
import com.webmotors.pagamentos.domain.model.PaymentMethod;
import com.webmotors.pagamentos.metrics.PaymentMetrics;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentApplicationService implements PaymentUseCase {
    private final PaymentRepositoryPort repository;
    private final PaymentProcessor processor;
    private final PaymentMetrics metrics;

    public PaymentApplicationService(PaymentRepositoryPort repository, PaymentProcessor processor, PaymentMetrics metrics) {
        this.repository = repository;
        this.processor = processor;
        this.metrics = metrics;
    }

    @Override
    public Payment create(Long usuarioId, BigDecimal amount, PaymentMethod method, String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey)
                .orElseGet(() -> {
                    metrics.registrarRecebido();
                    Payment pendente = repository.save(Payment.create(usuarioId, amount, method, idempotencyKey));
                    return processor.processar(pendente);
                });
    }

    @Override
    public List<Payment> findAll() {
        return repository.findAll();
    }
}