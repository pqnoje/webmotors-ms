package com.webmotors.pagamentos.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record Payment(
        Long id,
        Long usuarioId,
        BigDecimal amount,
        PaymentMethod method,
        String idempotencyKey,
        PaymentStatus status,
        OffsetDateTime createdAt
) {
    public static Payment create(Long usuarioId, BigDecimal amount, PaymentMethod method, String idempotencyKey) {
        return new Payment(null, usuarioId, amount, method, idempotencyKey, PaymentStatus.PENDING, OffsetDateTime.now());
    }

    public Payment withStatus(PaymentStatus newStatus) {
        return new Payment(id, usuarioId, amount, method, idempotencyKey, newStatus, createdAt);
    }
}