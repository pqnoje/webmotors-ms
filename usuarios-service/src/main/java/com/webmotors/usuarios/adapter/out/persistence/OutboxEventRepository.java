package com.webmotors.usuarios.adapter.out.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEventJpaEntity, Long> {
    List<OutboxEventJpaEntity> findByStatusOrderByIdAsc(String status, Pageable pageable);

    long countByStatus(String status);
}