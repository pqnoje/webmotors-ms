package com.webmotors.pagamentos.adapter.in.http.dto;

import com.webmotors.pagamentos.domain.model.Payment;
import com.webmotors.pagamentos.domain.model.PaymentMethod;
import com.webmotors.pagamentos.domain.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PaymentResponse(
        Long id,
        Long usuarioId,
        BigDecimal amount,
        PaymentMethod method,
        String idempotencyKey,
        PaymentStatus status,
        OffsetDateTime createdAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(payment.id(), payment.usuarioId(), payment.amount(), payment.method(),
                payment.idempotencyKey(), payment.status(), payment.createdAt());
    }
}