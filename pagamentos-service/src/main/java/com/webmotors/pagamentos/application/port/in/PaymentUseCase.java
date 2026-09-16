package com.webmotors.pagamentos.application.port.in;

import com.webmotors.pagamentos.domain.model.Payment;
import com.webmotors.pagamentos.domain.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentUseCase {
    Payment create(Long usuarioId, BigDecimal amount, PaymentMethod method, String idempotencyKey);

    List<Payment> findAll();
}