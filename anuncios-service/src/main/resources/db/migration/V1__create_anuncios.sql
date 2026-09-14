CREATE TABLE anuncios (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    marca VARCHAR(255) NOT NULL,
    modelo VARCHAR(255) NOT NULL,
    ano INTEGER NOT NULL,
    preco NUMERIC(10, 2) NOT NULL,
    descricao VARCHAR(1000)
);
