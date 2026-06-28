CREATE SCHEMA IF NOT EXISTS raizesnordeste;

CREATE TABLE raizesnordeste.contas
(
    id         UUID PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    status     VARCHAR(20)  NOT NULL,
    role       VARCHAR(20)  NOT NULL,

    CONSTRAINT chk_contas_role
        CHECK (role IN ('CLIENTE', 'OPERADOR', 'COZINHA', 'GERENTE', 'ADMINISTRADOR')),

    CONSTRAINT chk_contas_status
        CHECK (status IN ('ATIVA', 'DESATIVADA'))
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

CREATE TABLE raizesnordeste.pratos
(
    id             UUID PRIMARY KEY,
    unidade_id     UUID         NOT NULL REFERENCES raizesnordeste.unidades (id),
    nome           VARCHAR(150) NOT NULL,
    descricao      TEXT         NOT NULL,
    preco_centavos BIGINT       NOT NULL,
    disponivel     BOOLEAN      NOT NULL DEFAULT TRUE,
    ativo          BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE raizesnordeste.promocoes
(
    id                  UUID PRIMARY KEY,
    prato_id            UUID             NOT NULL REFERENCES raizesnordeste.pratos (id),
    descricao           TEXT             NOT NULL,
    percentual_desconto DOUBLE PRECISION NOT NULL,
    data_hora_inicio    TIMESTAMP        NOT NULL,
    data_hora_fim       TIMESTAMP        NOT NULL,
    ativa               BOOLEAN          NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_promocoes_prato_id ON raizesnordeste.promocoes (prato_id);

CREATE TABLE raizesnordeste.pedidos
(
    id                               UUID PRIMARY KEY,
    unidade_id                       UUID        NOT NULL REFERENCES raizesnordeste.unidades (id),
    cliente_id                       UUID REFERENCES raizesnordeste.clientes (id),
    cliente_fidelidade_id            UUID REFERENCES raizesnordeste.clientes (id),
    funcionario_id                   UUID REFERENCES raizesnordeste.funcionarios (id),
    nome_cliente                     VARCHAR(150),
    canal                            VARCHAR(20) NOT NULL,
    status                           VARCHAR(30) NOT NULL,
    pickup                           BOOLEAN     NOT NULL DEFAULT FALSE,
    horario_pedido                   TIMESTAMP   NOT NULL,
    horario_preparo                  TIMESTAMP,
    consentimento_fidelizacao        BOOLEAN     NOT NULL DEFAULT FALSE,
    valor_total_centavos             BIGINT      NOT NULL DEFAULT 0,
    valor_desconto_promocao_centavos BIGINT      NOT NULL DEFAULT 0,
    valor_desconto_pontos_centavos   BIGINT      NOT NULL DEFAULT 0,
    valor_final_centavos             BIGINT      NOT NULL DEFAULT 0,

    CONSTRAINT chk_pedidos_canal
        CHECK (canal IN ('APP', 'TOTEM', 'BALCAO')),

    CONSTRAINT chk_pedidos_status
        CHECK (status IN ('PAGAMENTO_PENDENTE', 'AGUARDANDO_PREPARO', 'EM_PREPARO', 'PRONTO', 'CONCLUIDO', 'CANCELADO'))
);

CREATE TABLE raizesnordeste.itens_pedido
(
    id                      UUID PRIMARY KEY,
    pedido_id               UUID   NOT NULL REFERENCES raizesnordeste.pedidos (id),
    prato_id                UUID   NOT NULL REFERENCES raizesnordeste.pratos (id),
    quantidade              INT    NOT NULL,
    preco_unitario_centavos BIGINT NOT NULL
);

CREATE INDEX idx_itens_pedido_pedido_id ON raizesnordeste.itens_pedido (pedido_id);

CREATE TABLE raizesnordeste.regras_fidelidade
(
    id                      UUID PRIMARY KEY,
    valor_por_ponto         NUMERIC(10, 6) NOT NULL,
    acumulo_por_centavo     NUMERIC(10, 6) NOT NULL,
    validade_pontos_meses   INT            NOT NULL,
    teto_resgate_percentual INT            NOT NULL,
    ativa                   BOOLEAN        NOT NULL DEFAULT FALSE,
    ativada_em              TIMESTAMP      NOT NULL,
    inativada_em            TIMESTAMP
);

CREATE UNIQUE INDEX idx_regras_fidelidade_ativa ON raizesnordeste.regras_fidelidade (ativa) WHERE ativa = TRUE;

CREATE TABLE raizesnordeste.movimentacoes_pontos
(
    id                  UUID PRIMARY KEY,
    cliente_id          UUID        NOT NULL REFERENCES raizesnordeste.clientes (id),
    pedido_id           UUID REFERENCES raizesnordeste.pedidos (id),
    tipo                VARCHAR(20) NOT NULL,
    pontos              BIGINT      NOT NULL,
    data_contabilizacao TIMESTAMP   NOT NULL,
    data_expiracao      TIMESTAMP,

    CONSTRAINT chk_movimentacoes_pontos_tipo
        CHECK (tipo IN ('ACUMULO', 'RESGATE', 'EXPIRACAO'))
);

CREATE INDEX idx_movimentacoes_pontos_cliente_id ON raizesnordeste.movimentacoes_pontos (cliente_id);

CREATE TABLE raizesnordeste.pagamentos
(
    id                   UUID PRIMARY KEY,
    pedido_id            UUID        NOT NULL REFERENCES raizesnordeste.pedidos (id),
    idempotency_key      VARCHAR(255),
    forma                VARCHAR(20) NOT NULL,
    status               VARCHAR(20) NOT NULL,
    valor_centavos       BIGINT      NOT NULL,
    id_transacao_gateway VARCHAR(255),
    data_solicitacao     TIMESTAMP   NOT NULL,
    data_confirmacao     TIMESTAMP,
    motivo_recusa        VARCHAR(255),
    qr_code              TEXT,
    qr_code_valido_ate   TIMESTAMP,

    CONSTRAINT chk_pagamentos_forma
        CHECK (forma IN ('PIX', 'CARTAO_CREDITO', 'CARTAO_DEBITO')),

    CONSTRAINT chk_pagamentos_status
        CHECK (status IN ('PENDENTE', 'APROVADO', 'RECUSADO', 'ERRO'))
);

CREATE INDEX idx_pagamentos_pedido_id ON raizesnordeste.pagamentos (pedido_id);

CREATE UNIQUE INDEX idx_pagamentos_id_transacao_gateway
    ON raizesnordeste.pagamentos (id_transacao_gateway)
    WHERE id_transacao_gateway IS NOT NULL;

CREATE UNIQUE INDEX idx_pagamentos_idempotency_key
    ON raizesnordeste.pagamentos (idempotency_key)
    WHERE idempotency_key IS NOT NULL;

CREATE TABLE raizesnordeste.refresh_tokens
(
    id          UUID PRIMARY KEY,
    conta_id    UUID        NOT NULL REFERENCES raizesnordeste.contas (id),
    token_hash  VARCHAR(64) NOT NULL UNIQUE,
    expira_em   TIMESTAMP   NOT NULL,
    revogado_em TIMESTAMP
);

CREATE INDEX idx_refresh_tokens_conta_id ON raizesnordeste.refresh_tokens (conta_id);
