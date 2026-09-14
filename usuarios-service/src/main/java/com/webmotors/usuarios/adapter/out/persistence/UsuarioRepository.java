package com.webmotors.usuarios.adapter.out.persistence;

import com.webmotors.usuarios.adapter.out.persistence.UsuarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioJpaEntity, Long> {
    Optional<UsuarioJpaEntity> findByEmail(String email);
}
