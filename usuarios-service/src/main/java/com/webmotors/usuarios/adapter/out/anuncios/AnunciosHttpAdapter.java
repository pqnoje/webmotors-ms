package com.webmotors.usuarios.adapter.out.anuncios;

import com.webmotors.usuarios.application.port.out.AnuncioQueryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

@Component
public class AnunciosHttpAdapter implements AnuncioQueryPort {
    private static final ParameterizedTypeReference<List<Long>> IDS_TYPE =
        new ParameterizedTypeReference<>() {
        };

    private final RestClient restClient;

    public AnunciosHttpAdapter(
        RestClient.Builder restClientBuilder,
        @Value("${anuncios-service.url:http://localhost:8082}") String anunciosServiceUrl
    ) {
        this.restClient = restClientBuilder.baseUrl(Objects.requireNonNull(anunciosServiceUrl)).build();
    }

    @Override
    public List<Long> findUsuarioIdsPorMarca(String marca) {
        List<Long> ids = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/anuncios/usuarios-por-marca")
                .queryParam("marca", marca)
                .build())
            .retrieve()
            .body(Objects.requireNonNull(IDS_TYPE));
        return ids == null ? List.of() : ids;
    }
}