package com.webmotors.anuncios.adapter.out.persistence;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "anuncios")
public class AnuncioJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false)
    private boolean ativo;

    @Column(length = 50)
    private String motivoDesativacao;

    protected AnuncioJpaEntity() {
    }

    public AnuncioJpaEntity(Long id, Long usuarioId, String marca, String modelo, Integer ano,
                            BigDecimal preco, String descricao, boolean ativo) {
        this(id, usuarioId, marca, modelo, ano, preco, descricao, ativo, null);
    }

    public AnuncioJpaEntity(Long id, Long usuarioId, String marca, String modelo, Integer ano,
                            BigDecimal preco, String descricao, boolean ativo, String motivoDesativacao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.preco = preco;
        this.descricao = descricao;
        this.ativo = ativo;
        this.motivoDesativacao = motivoDesativacao;
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