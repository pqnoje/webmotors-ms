package com.webmotors.pagamentos.application.port.out;

import com.webmotors.pagamentos.domain.model.Payment;
import com.webmotors.pagamentos.domain.model.PaymentStatus;

import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryPort {
    Payment save(Payment payment);

    Optional<Payment> findByIdempotencyKey(String idempotencyKey);

    List<Payment> findAll();

    List<Payment> findByStatus(PaymentStatus status);
}