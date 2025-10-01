-- ==========================
-- Segurança (tabelas + seeds)
-- ==========================

CREATE TABLE TB_TIPO_USUARIO (
                                 ID_TIPO_USUARIO INTEGER PRIMARY KEY,
                                 DESCRICAO       VARCHAR2(50) NOT NULL
);

CREATE TABLE TB_USUARIO (
                            ID_USUARIO       INTEGER PRIMARY KEY,
                            NOME             VARCHAR2(100) NOT NULL,
                            EMAIL            VARCHAR2(120) NOT NULL UNIQUE,
                            SENHA            VARCHAR2(200) NOT NULL,
                            ID_TIPO_USUARIO  INTEGER NOT NULL
);

-- Perfis
INSERT INTO TB_TIPO_USUARIO (ID_TIPO_USUARIO, DESCRICAO) VALUES (1, 'ADMINISTRADOR');
INSERT INTO TB_TIPO_USUARIO (ID_TIPO_USUARIO, DESCRICAO) VALUES (2, 'OPERADOR');

-- Usuários (ajuste senha conforme encoder)
-- Se estiver usando NoOpPasswordEncoder (dev): SENHA em texto puro.
-- Se estiver usando BCrypt: colocar o hash.
INSERT INTO TB_USUARIO (ID_USUARIO, NOME, EMAIL, SENHA, ID_TIPO_USUARIO)
VALUES (1, 'Admin', 'admin@ex.com', 'fiap25', 1);

INSERT INTO TB_USUARIO (ID_USUARIO, NOME, EMAIL, SENHA, ID_TIPO_USUARIO)
VALUES (2, 'Operador', 'joao@ex.com', 'fiap25', 2);
