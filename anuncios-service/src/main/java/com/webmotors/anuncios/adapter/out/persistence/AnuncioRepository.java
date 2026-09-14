package com.webmotors.anuncios.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnuncioRepository extends JpaRepository<AnuncioJpaEntity, Long> {
    List<AnuncioJpaEntity> findByUsuarioId(Long usuarioId);

    @Query("select distinct a.usuarioId from AnuncioJpaEntity a where lower(a.marca) = lower(:marca)")
    List<Long> findDistinctUsuarioIdsByMarca(@Param("marca") String marca);

    @Modifying
    @Query("update AnuncioJpaEntity a set a.ativo = false, a.motivoDesativacao = 'USUARIO_DESATIVADO' where a.usuarioId = :usuarioId and a.ativo = true")
    int desativarPorUsuarioId(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query("update AnuncioJpaEntity a set a.ativo = false, a.motivoDesativacao = 'BLOQUEIO_LEGAL_MARCA' where a.usuarioId in :usuarioIds and a.ativo = true")
    int desativarPorUsuarioIds(@Param("usuarioIds") List<Long> usuarioIds);
}