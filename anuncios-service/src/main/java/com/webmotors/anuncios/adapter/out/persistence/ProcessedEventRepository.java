package com.webmotors.anuncios.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEventJpaEntity, String> {
}