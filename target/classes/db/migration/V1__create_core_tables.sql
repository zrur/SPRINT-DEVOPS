-- ==========================
-- CORE TABLES (sem segurança)
-- ==========================

-- Endereços/Localidades
CREATE TABLE TB_PAIS (
                         ID_PAIS        INTEGER PRIMARY KEY,
                         NOME           VARCHAR2(100)
);

CREATE TABLE TB_ESTADO (
                           ID_ESTADO      INTEGER PRIMARY KEY,
                           NOME           VARCHAR2(100),
                           SIGLA          VARCHAR2(2),
                           ID_PAIS        INTEGER
);

CREATE TABLE TB_CIDADE (
                           ID_CIDADE      INTEGER PRIMARY KEY,
                           NOME           VARCHAR2(120),
                           ID_ESTADO      INTEGER
);

-- Cadastros principais
CREATE TABLE TB_CLIENTE (
                            ID_CLIENTE     INTEGER PRIMARY KEY,
                            NOME           VARCHAR2(120) NOT NULL
);

CREATE TABLE TB_MODELO_MOTO (
                                ID_MODELO_MOTO INTEGER PRIMARY KEY,
                                NOME           VARCHAR2(80)  NOT NULL,
                                FABRICANTE     VARCHAR2(80)
);

CREATE TABLE TB_MODELO_BEACON (
                                  ID_MODELO_BEACON INTEGER PRIMARY KEY,
                                  NOME             VARCHAR2(80) NOT NULL,
                                  FABRICANTE       VARCHAR2(80)
);

CREATE TABLE TB_MOTO (
                         ID_MOTO        INTEGER PRIMARY KEY,
                         PLACA          VARCHAR2(10)  NOT NULL,
                         ID_CLIENTE     INTEGER,
                         ID_MODELO_MOTO INTEGER
);

CREATE TABLE TB_BEACON (
                           ID_BEACON         INTEGER PRIMARY KEY,
                           UUID              VARCHAR2(100) NOT NULL,
                           BATERIA           INTEGER,
                           ID_MOTO           INTEGER,
                           ID_MODELO_BEACON  INTEGER
);

-- Operação
CREATE TABLE TB_PATIO (
                          ID_PATIO        INTEGER PRIMARY KEY,
                          NOME            VARCHAR2(80) NOT NULL
);

CREATE TABLE TB_TIPO_MOVIMENTACAO (
                                      ID_TIPO_MOVIMENTACAO INTEGER PRIMARY KEY,
                                      DESCRICAO            VARCHAR2(50) NOT NULL
);

CREATE TABLE TB_MOVIMENTACAO (
                                 ID_MOVIMENTACAO      INTEGER PRIMARY KEY,
                                 DATA_MOVIMENTACAO    TIMESTAMP,
                                 OBSERVACAO           CLOB,
                                 ID_MOTO              INTEGER,
                                 ID_TIPO_MOVIMENTACAO INTEGER
);

CREATE TABLE TB_LOCALIZACAO (
                                ID_LOCALIZACAO   INTEGER PRIMARY KEY,
                                POSICAO_X        NUMBER,
                                POSICAO_Y        NUMBER,
                                DATA_HORA        TIMESTAMP,
                                ID_MOTO          INTEGER,
                                ID_PATIO         INTEGER
);

-- Auditoria/monitoramento
CREATE TABLE TB_LOG_SISTEMA (
                                ID_LOG        INTEGER PRIMARY KEY,
                                DATA_HORA     TIMESTAMP,
                                ENTIDADE      VARCHAR2(60),
                                ACAO          VARCHAR2(40),
                                DESCRICAO     CLOB
);

CREATE TABLE TB_REGISTRO_BATERIA (
                                     ID_REGISTRO_BATERIA INTEGER PRIMARY KEY,
                                     ID_BEACON           INTEGER,
                                     NIVEL               INTEGER,
                                     DATA_HORA           TIMESTAMP
);
