package com.webmotors.usuarios.application.port.out;

import java.util.List;

public interface AnuncioQueryPort {
    List<Long> findUsuarioIdsPorMarca(String marca);
}