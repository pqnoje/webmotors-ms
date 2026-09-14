package com.webmotors.anuncios.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class Anuncio {
    private final Long id;
    private final Long usuarioId;
    private final String marca;
    private final String modelo;
    private final Integer ano;
    private final BigDecimal preco;
    private final String descricao;
    private final boolean ativo;
    private final String motivoDesativacao;

    private Anuncio(Long id, Long usuarioId, String marca, String modelo, Integer ano,
                    BigDecimal preco, String descricao, boolean ativo, String motivoDesativacao) {
        this.id = id;
        this.usuarioId = Objects.requireNonNull(usuarioId);
        this.marca = Objects.requireNonNull(marca);
        this.modelo = Objects.requireNonNull(modelo);
        this.ano = Objects.requireNonNull(ano);
        this.preco = Objects.requireNonNull(preco);
        this.descricao = descricao;
        this.ativo = ativo;
        this.motivoDesativacao = motivoDesativacao;
    }

    public static Anuncio criar(Long usuarioId, String marca, String modelo, Integer ano,
                                BigDecimal preco, String descricao) {
        return new Anuncio(null, usuarioId, marca, modelo, ano, preco, descricao, true, null);
    }

    public static Anuncio reconstituir(Long id, Long usuarioId, String marca, String modelo,
                                       Integer ano, BigDecimal preco, String descricao,
                                       boolean ativo) {
        return reconstituir(id, usuarioId, marca, modelo, ano, preco, descricao, ativo, null);
    }

    public static Anuncio reconstituir(Long id, Long usuarioId, String marca, String modelo,
                                       Integer ano, BigDecimal preco, String descricao,
                                       boolean ativo, String motivoDesativacao) {
        return new Anuncio(
            Objects.requireNonNull(id), usuarioId, marca, modelo, ano, preco, descricao,
            ativo, motivoDesativacao
        );
    }

    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public Integer getAno() { return ano; }
    public BigDecimal getPreco() { return preco; }
    public String getDescricao() { return descricao; }
    public boolean isAtivo() { return ativo; }
    public String getMotivoDesativacao() { return motivoDesativacao; }
}