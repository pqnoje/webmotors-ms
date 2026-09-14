package com.webmotors.anuncios.adapter.in.http;

import com.webmotors.anuncios.adapter.in.http.dto.AnuncioResponse;
import com.webmotors.anuncios.adapter.in.http.dto.CriarAnuncioRequest;
import com.webmotors.anuncios.application.port.in.AnuncioUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {
    private final AnuncioUseCase useCase;

    public AnuncioController(AnuncioUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnuncioResponse criar(@Valid @RequestBody CriarAnuncioRequest request) {
        return AnuncioResponse.from(useCase.criar(request.toCommand()));
    }

    @GetMapping
    public List<AnuncioResponse> listar() {
        return useCase.listar().stream().map(AnuncioResponse::from).toList();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<AnuncioResponse> listarPorUsuario(@PathVariable Long usuarioId) {
        return useCase.listarPorUsuario(usuarioId).stream().map(AnuncioResponse::from).toList();
    }

    @GetMapping("/usuarios-por-marca")
    public List<Long> listarUsuarioIdsPorMarca(@RequestParam String marca) {
        return useCase.listarUsuarioIdsPorMarca(marca);
    }
}
