package com.webmotors.usuarios.application.port.in;

import com.webmotors.usuarios.domain.model.Usuario;

import java.util.List;

public interface UsuarioUseCase {
    Usuario criar(CriarUsuarioCommand command);

    List<Usuario> listar();

    Usuario desativar(Long id);

    DesativacaoEmMassaResult desativarEmMassa(List<Long> ids);

    DesativacaoEmMassaResult desativarPorMarcaProibida(String marca);

    record CriarUsuarioCommand(String nome, String email) {
    }

    record DesativacaoEmMassaResult(
        int total,
        int processados,
        List<Falha> falhas
    ) {
        public record Falha(Long usuarioId, String mensagem) {
        }
    }
}