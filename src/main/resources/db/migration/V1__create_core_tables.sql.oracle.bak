-- ==========================
-- CORE TABLES (sem segurança)
-- ==========================

-- Endereços/Localidades
CREATE TABLE TB_PAIS (
                         ID_PAIS        INT IDENTITY(1,1) PRIMARY KEY,
                         NOME           VARCHAR(100)
);

CREATE TABLE TB_ESTADO (
                           ID_ESTADO      INT IDENTITY(1,1) PRIMARY KEY,
                           NOME           VARCHAR(100),
                           SIGLA          VARCHAR(2),
                           ID_PAIS        INTEGER
);

CREATE TABLE TB_CIDADE (
                           ID_CIDADE      INT IDENTITY(1,1) PRIMARY KEY,
                           NOME           VARCHAR(120),
                           ID_ESTADO      INTEGER
);

-- Cadastros principais
CREATE TABLE TB_CLIENTE (
                            ID_CLIENTE     INT IDENTITY(1,1) PRIMARY KEY,
                            NOME           VARCHAR(120) NOT NULL
);

CREATE TABLE TB_MODELO_MOTO (
                                ID_MODELO_MOTO INT IDENTITY(1,1) PRIMARY KEY,
                                NOME           VARCHAR(80)  NOT NULL,
                                FABRICANTE     VARCHAR(80)
);

CREATE TABLE TB_MODELO_BEACON (
                                  ID_MODELO_BEACON INT IDENTITY(1,1) PRIMARY KEY,
                                  NOME             VARCHAR(80) NOT NULL,
                                  FABRICANTE       VARCHAR(80)
);

CREATE TABLE TB_MOTO (
                         ID_MOTO        INT IDENTITY(1,1) PRIMARY KEY,
                         PLACA          VARCHAR(10)  NOT NULL,
                         ID_CLIENTE     INTEGER,
                         ID_MODELO_MOTO INTEGER
);

CREATE TABLE TB_BEACON (
                           ID_BEACON         INT IDENTITY(1,1) PRIMARY KEY,
                           UUID              VARCHAR(100) NOT NULL,
                           BATERIA           INTEGER,
                           ID_MOTO           INTEGER,
                           ID_MODELO_BEACON  INTEGER
);

-- Operação
CREATE TABLE TB_PATIO (
                          ID_PATIO        INT IDENTITY(1,1) PRIMARY KEY,
                          NOME            VARCHAR(80) NOT NULL
);

CREATE TABLE TB_TIPO_MOVIMENTACAO (
                                      ID_TIPO_MOVIMENTACAO INT IDENTITY(1,1) PRIMARY KEY,
                                      DESCRICAO            VARCHAR(50) NOT NULL
);

CREATE TABLE TB_MOVIMENTACAO (
                                 ID_MOVIMENTACAO      INT IDENTITY(1,1) PRIMARY KEY,
                                 DATA_MOVIMENTACAO    DATETIME,
                                 OBSERVACAO           VARCHAR(MAX),
                                 ID_MOTO              INTEGER,
                                 ID_TIPO_MOVIMENTACAO INTEGER
);

CREATE TABLE TB_LOCALIZACAO (
                                ID_LOCALIZACAO   INT IDENTITY(1,1) PRIMARY KEY,
                                POSICAO_X        INT,
                                POSICAO_Y        INT,
                                DATA_HORA        DATETIME,
                                ID_MOTO          INTEGER,
                                ID_PATIO         INTEGER
);

-- Auditoria/monitoramento
CREATE TABLE TB_LOG_SISTEMA (
                                ID_LOG        INT IDENTITY(1,1) PRIMARY KEY,
                                DATA_HORA     DATETIME,
                                ENTIDADE      VARCHAR(60),
                                ACAO          VARCHAR(40),
                                DESCRICAO     VARCHAR(MAX)
);

CREATE TABLE TB_REGISTRO_BATERIA (
                                     ID_REGISTRO_BATERIA INT IDENTITY(1,1) PRIMARY KEY,
                                     ID_BEACON           INTEGER,
                                     NIVEL               INTEGER,
                                     DATA_HORA           DATETIME
);
