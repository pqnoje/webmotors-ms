package com.webmotors.usuarios.domain.model;

import java.util.Objects;

public final class Usuario {
    private final Long id;
    private final String nome;
    private final String email;
    private boolean ativo;

    private Usuario(Long id, String nome, String email, boolean ativo) {
        this.id = id;
        this.nome = Objects.requireNonNull(nome);
        this.email = Objects.requireNonNull(email);
        this.ativo = ativo;
    }

    public static Usuario criar(String nome, String email) {
        return new Usuario(null, nome, email, true);
    }

    public static Usuario reconstituir(Long id, String nome, String email, boolean ativo) {
        return new Usuario(Objects.requireNonNull(id), nome, email, ativo);
    }

    public void desativar() {
        ativo = false;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public boolean isAtivo() { return ativo; }
}