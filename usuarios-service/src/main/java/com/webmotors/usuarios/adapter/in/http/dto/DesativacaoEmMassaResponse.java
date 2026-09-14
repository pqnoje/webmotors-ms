package com.webmotors.usuarios.adapter.in.http.dto;

import com.webmotors.usuarios.application.port.in.UsuarioUseCase;

import java.util.List;

public record DesativacaoEmMassaResponse(
    int total,
    int processados,
    List<FalhaResponse> falhas
) {
    public static DesativacaoEmMassaResponse from(UsuarioUseCase.DesativacaoEmMassaResult result) {
        return new DesativacaoEmMassaResponse(
            result.total(),
            result.processados(),
            result.falhas().stream().map(FalhaResponse::from).toList()
        );
    }

    public record FalhaResponse(Long usuarioId, String mensagem) {
        private static FalhaResponse from(UsuarioUseCase.DesativacaoEmMassaResult.Falha falha) {
            return new FalhaResponse(falha.usuarioId(), falha.mensagem());
        }
    }
}