package com.webmotors.pagamentos.adapter.out.persistence;

import com.webmotors.pagamentos.application.port.out.PaymentRepositoryPort;
import com.webmotors.pagamentos.domain.model.Payment;
import com.webmotors.pagamentos.domain.model.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentPersistenceAdapter implements PaymentRepositoryPort {
    private final PaymentRepository repository;

    public PaymentPersistenceAdapter(PaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Payment save(Payment payment) {
        return repository.save(PaymentJpaEntity.from(payment)).toDomain();
    }

    @Override
    public Optional<Payment> findByIdempotencyKey(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey).map(PaymentJpaEntity::toDomain);
    }

    @Override
    public List<Payment> findAll() {
        return repository.findAll().stream().map(PaymentJpaEntity::toDomain).toList();
    }

    @Override
    public List<Payment> findByStatus(PaymentStatus status) {
        return repository.findByStatus(status).stream().map(PaymentJpaEntity::toDomain).toList();
    }
}