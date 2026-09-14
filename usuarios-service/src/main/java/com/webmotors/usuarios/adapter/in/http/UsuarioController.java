package com.webmotors.usuarios.adapter.in.http;

import com.webmotors.usuarios.adapter.in.http.dto.CriarUsuarioRequest;
import com.webmotors.usuarios.adapter.in.http.dto.DesativacaoEmMassaRequest;
import com.webmotors.usuarios.adapter.in.http.dto.DesativacaoEmMassaResponse;
import com.webmotors.usuarios.adapter.in.http.dto.DesativacaoPorMarcaRequest;
import com.webmotors.usuarios.adapter.in.http.dto.UsuarioResponse;
import com.webmotors.usuarios.application.port.in.UsuarioUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioUseCase useCase;

    public UsuarioController(UsuarioUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public UsuarioResponse criar(@Valid @RequestBody CriarUsuarioRequest request) {
        return UsuarioResponse.from(useCase.criar(request.toCommand()));
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return useCase.listar().stream().map(UsuarioResponse::from).toList();
    }

    @PatchMapping("/{id}/desativar")
    public UsuarioResponse desativar(@PathVariable Long id) {
        return UsuarioResponse.from(useCase.desativar(id));
    }

    @PostMapping("/desativacao-em-massa")
    public DesativacaoEmMassaResponse desativarEmMassa(
        @Valid @RequestBody DesativacaoEmMassaRequest request
    ) {
        return DesativacaoEmMassaResponse.from(useCase.desativarEmMassa(request.ids()));
    }

    @PostMapping("/desativacao-por-marca")
    public DesativacaoEmMassaResponse desativarPorMarca(
        @Valid @RequestBody DesativacaoPorMarcaRequest request
    ) {
        return DesativacaoEmMassaResponse.from(useCase.desativarPorMarcaProibida(request.marca()));
    }
}
