-- ATENÇÃO: arquivo de seed apenas para ambiente de teste. Não deve ser utilizado em produção.

INSERT INTO raizesnordeste.unidades (id, nome, endereco, hora_de, hora_ate, ativa)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Unidade Volta Redonda', 'Rua 33, Vila, 100, Volta Redonda - RJ', 8, 22,
        TRUE) ON CONFLICT (id) DO NOTHING;

-- Senha do admin de teste: Senha@123
INSERT INTO raizesnordeste.contas (id, email, senha_hash, status, role)
VALUES ('10de2644-f7eb-4182-90c3-d7048238d34b',
        'admin@raizesnordeste.com',
        '$argon2id$v=19$m=65536,t=3,p=1$Csi/+A+rU9PKTiPDtWK7gg$rZhOzMSK4xdxSdnojYcCvmKzl9VguhfNq0KOQeB9R6c',
        'ATIVA',
        'ADMINISTRADOR') ON CONFLICT (id) DO NOTHING;