CREATE SCHEMA IF NOT EXISTS raizesnordeste;

CREATE TABLE raizesnordeste.contas
(
    id         UUID PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    status     VARCHAR(20)  NOT NULL,
    role       VARCHAR(20)  NOT NULL
);

CREATE TABLE raizesnordeste.clientes
(
    id                 UUID PRIMARY KEY,
    conta_id           UUID         NOT NULL REFERENCES raizesnordeste.contas (id),
    nome               VARCHAR(150) NOT NULL,
    cpf                VARCHAR(11)  NOT NULL UNIQUE,
    telefone           VARCHAR(11)  NOT NULL,
    endereco           TEXT         NOT NULL,
    data_nascimento    DATE         NOT NULL,
    saldo_pontos       BIGINT       NOT NULL DEFAULT 0,
    aceite_termos      BOOLEAN      NOT NULL DEFAULT FALSE,
    data_aceite_termos TIMESTAMP,
    versao_termos      VARCHAR(20),
    data_cadastro      TIMESTAMP    NOT NULL
);

CREATE TABLE raizesnordeste.unidades
(
    id       UUID PRIMARY KEY,
    nome     VARCHAR(150) NOT NULL,
    endereco TEXT         NOT NULL,
    hora_de  SMALLINT     NOT NULL,
    hora_ate SMALLINT     NOT NULL,
    ativa    BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE raizesnordeste.funcionarios
(
    id              UUID PRIMARY KEY,
    conta_id        UUID         NOT NULL REFERENCES raizesnordeste.contas (id),
    unidade_id      UUID         NOT NULL REFERENCES raizesnordeste.unidades (id),
    nome            VARCHAR(150) NOT NULL,
    telefone        VARCHAR(11)  NOT NULL,
    endereco        TEXT         NOT NULL,
    data_nascimento DATE         NOT NULL
);

CREATE TABLE raizesnordeste.refresh_tokens
(
    id          UUID PRIMARY KEY,
    conta_id    UUID        NOT NULL REFERENCES raizesnordeste.contas (id),
    token_hash  VARCHAR(64) NOT NULL UNIQUE,
    expira_em   TIMESTAMP   NOT NULL,
    revogado_em TIMESTAMP
);

CREATE INDEX idx_refresh_tokens_conta_id ON raizesnordeste.refresh_tokens (conta_id);
