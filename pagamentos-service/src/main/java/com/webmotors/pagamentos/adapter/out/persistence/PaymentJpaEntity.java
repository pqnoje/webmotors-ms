package com.webmotors.pagamentos.adapter.out.persistence;

import com.webmotors.pagamentos.domain.model.Payment;
import com.webmotors.pagamentos.domain.model.PaymentMethod;
import com.webmotors.pagamentos.domain.model.PaymentStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "pagamentos")
public class PaymentJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;
    @Column(name = "idempotency_key", nullable = false, unique = true, length = 100)
    private String idempotencyKey;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected PaymentJpaEntity() {
    }

    private PaymentJpaEntity(Payment payment) {
        this.id = payment.id();
        this.usuarioId = payment.usuarioId();
        this.amount = payment.amount();
        this.method = payment.method();
        this.idempotencyKey = payment.idempotencyKey();
        this.status = payment.status();
        this.createdAt = payment.createdAt();
    }

    public static PaymentJpaEntity from(Payment payment) {
        return new PaymentJpaEntity(payment);
    }

    public Payment toDomain() {
        return new Payment(id, usuarioId, amount, method, idempotencyKey, status, createdAt);
    }
}