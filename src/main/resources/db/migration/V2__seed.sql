INSERT INTO raizesnordeste.bases (id, nome, slug, roles_permitidas)
VALUES (gen_random_uuid(), 'Clientes', 'clientes', ARRAY['CLIENTE']) ON CONFLICT (slug) DO NOTHING;
