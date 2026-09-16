package com.webmotors.pagamentos.adapter.out.persistence;

import com.webmotors.pagamentos.domain.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentJpaEntity, Long> {
    Optional<PaymentJpaEntity> findByIdempotencyKey(String idempotencyKey);

    List<PaymentJpaEntity> findByStatus(PaymentStatus status);
}