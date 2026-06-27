-- ATENÇÃO: arquivo de seed apenas para ambiente de teste. Não deve ser utilizado em produção.

INSERT INTO raizesnordeste.unidades (id, nome, endereco, hora_de, hora_ate, ativa)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Unidade Volta Redonda', 'Rua 33, Vila, 100, Volta Redonda - RJ', 8, 22,
        TRUE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO raizesnordeste.pratos (id, unidade_id, nome, descricao, preco_centavos, disponivel, ativo)
VALUES ('581fd7ef-56d3-4ac4-832b-b8953c3736fb',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        'Acarajé',
        'Bolinho feito de massa de feijão-fradinho, cebola e sal, e frito em azeite de dendê',
        3590,
        TRUE,
        TRUE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO raizesnordeste.pratos (id, unidade_id, nome, descricao, preco_centavos, disponivel, ativo)
VALUES ('a737a7e6-c0fe-4e19-9f56-8d02edf6b99d',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        'Carne de Sol com Macaxeira',
        'A combinação perfeita entre o salgadinho da carne e a textura macia e adocicada da mandioca cozida ou frita agrada a diversos paladares.',
        4160,
        TRUE,
        TRUE)
ON CONFLICT (id) DO NOTHING;

-- Promoção ativa: 15% de desconto na Carne de Sol com Macaxeira
INSERT INTO raizesnordeste.promocoes (id, prato_id, descricao, percentual_desconto, data_hora_inicio, data_hora_fim,
                                      ativa)
VALUES ('f7fe78b1-2d3b-421e-9887-2d57176b2ebe',
        'a737a7e6-c0fe-4e19-9f56-8d02edf6b99d',
        'Super descontão de 15% na Carne de Sol com Macaxeira',
        15.0,
        '2026-01-01 00:00:00',
        '2099-12-31 23:59:59',
        TRUE)
ON CONFLICT (id) DO NOTHING;

-- Promoção expirada: 10% de desconto na Carne de Sol com Macaxeira
INSERT INTO raizesnordeste.promocoes (id, prato_id, descricao, percentual_desconto, data_hora_inicio, data_hora_fim,
                                      ativa)
VALUES ('470cdd96-e2e5-499b-98fc-5f9243f4cebe',
        'a737a7e6-c0fe-4e19-9f56-8d02edf6b99d',
        'Promoção imperdível 10% de desconto na Carne de Sol com Macaxeira',
        10.0,
        '2025-01-01 00:00:00',
        '2025-12-31 23:59:59',
        FALSE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO raizesnordeste.regras_fidelidade (id, valor_por_ponto, acumulo_por_centavo, validade_pontos_meses,
                                              teto_resgate_percentual, ativa, ativada_em)
VALUES ('78d36ce1-d755-4f1c-95ae-f941ed5df80c',
        0.010000,
        0.020000,
        6,
        20,
        TRUE,
        NOW())
ON CONFLICT (id) DO NOTHING;

-- Senha do admin de teste: Senha@123
INSERT INTO raizesnordeste.contas (id, email, senha_hash, status, role)
VALUES ('10de2644-f7eb-4182-90c3-d7048238d34b',
        'admin@raizesnordeste.com',
        '$argon2id$v=19$m=65536,t=3,p=1$Csi/+A+rU9PKTiPDtWK7gg$rZhOzMSK4xdxSdnojYcCvmKzl9VguhfNq0KOQeB9R6c',
        'ATIVA',
        'ADMINISTRADOR')
ON CONFLICT (id) DO NOTHING;

-- Senha do cliente de teste: Senha@123
INSERT INTO raizesnordeste.contas (id, email, senha_hash, status, role)
VALUES ('6b3b02b9-89e1-4b91-b51c-10c02130c4fd',
        'fidelidade@email.com',
        '$argon2id$v=19$m=65536,t=3,p=1$Csi/+A+rU9PKTiPDtWK7gg$rZhOzMSK4xdxSdnojYcCvmKzl9VguhfNq0KOQeB9R6c',
        'ATIVA',
        'CLIENTE')
ON CONFLICT (id) DO NOTHING;

INSERT INTO raizesnordeste.clientes (id, conta_id, nome, cpf, telefone, endereco, data_nascimento,
                                     saldo_pontos, aceite_termos, data_aceite_termos, versao_termos, data_cadastro)
VALUES ('c18fded1-99db-4280-9d2a-549216188472',
        '6b3b02b9-89e1-4b91-b51c-10c02130c4fd',
        'Cliente Fidelidade', '96057085078', '11987654321', 'Rua Fidelidade, 1', '1995-01-01',
        100000, TRUE, NOW(), 'v1.0', NOW())
ON CONFLICT (id) DO NOTHING;

-- Senha do cliente de teste: Senha@123
-- Cliente com saldo baixo para o cenário de resgate sem saldo suficiente
INSERT INTO raizesnordeste.contas (id, email, senha_hash, status, role)
VALUES ('cb96ee09-2d50-455c-8619-05f08bbeeefe',
        'saldobaixo@email.com',
        '$argon2id$v=19$m=65536,t=3,p=1$Csi/+A+rU9PKTiPDtWK7gg$rZhOzMSK4xdxSdnojYcCvmKzl9VguhfNq0KOQeB9R6c',
        'ATIVA',
        'CLIENTE')
ON CONFLICT (id) DO NOTHING;

INSERT INTO raizesnordeste.clientes (id, conta_id, nome, cpf, telefone, endereco, data_nascimento,
                                     saldo_pontos, aceite_termos, data_aceite_termos, versao_termos, data_cadastro)
VALUES ('e0b4d022-4c00-46fe-b213-ae2682b7e689',
        'cb96ee09-2d50-455c-8619-05f08bbeeefe',
        'Cliente Saldo Baixo', '77703321095', '11987650000', 'Rua Saldo Baixo, 1', '1995-01-01',
        50, TRUE, NOW(), 'v1.0', NOW())
ON CONFLICT (id) DO NOTHING;