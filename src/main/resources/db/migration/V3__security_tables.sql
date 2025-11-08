-- ==========================
-- Segurança (tabelas + seeds)
-- ==========================

CREATE TABLE TB_TIPO_USUARIO (
                                 ID_TIPO_USUARIO INT IDENTITY(1,1) PRIMARY KEY,
                                 DESCRICAO       VARCHAR(50) NOT NULL
);

CREATE TABLE TB_USUARIO (
                            ID_USUARIO       INT IDENTITY(1,1) PRIMARY KEY,
                            NOME             VARCHAR(100) NOT NULL,
                            EMAIL            VARCHAR(120) NOT NULL UNIQUE,
                            SENHA            VARCHAR(200) NOT NULL,
                            ID_TIPO_USUARIO  INTEGER NOT NULL
);

-- Perfis
SET IDENTITY_INSERT TB_TIPO_USUARIO ON;
INSERT INTO TB_TIPO_USUARIO (ID_TIPO_USUARIO, DESCRICAO) VALUES (1, 'ADMINISTRADOR');
INSERT INTO TB_TIPO_USUARIO (ID_TIPO_USUARIO, DESCRICAO) VALUES (2, 'OPERADOR');
SET IDENTITY_INSERT TB_TIPO_USUARIO OFF;

-- Usuários (ajuste senha conforme encoder)
-- Se estiver usando NoOpPasswordEncoder (dev): SENHA em texto puro.
-- Se estiver usando BCrypt: colocar o hash.
SET IDENTITY_INSERT TB_USUARIO ON;
INSERT INTO TB_USUARIO (ID_USUARIO, NOME, EMAIL, SENHA, ID_TIPO_USUARIO)
VALUES (1, 'Admin', 'admin@ex.com', 'fiap25', 1);

INSERT INTO TB_USUARIO (ID_USUARIO, NOME, EMAIL, SENHA, ID_TIPO_USUARIO)
VALUES (2, 'Operador', 'joao@ex.com', 'fiap25', 2);
SET IDENTITY_INSERT TB_USUARIO OFF;
