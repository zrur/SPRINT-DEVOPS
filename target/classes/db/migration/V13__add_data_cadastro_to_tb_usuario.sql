------------------------------------------------------------
-- V13__add_data_cadastro_to_tb_usuario.sql
-- Adiciona a coluna DATA_CADASTRO à tabela TB_USUARIO
------------------------------------------------------------

ALTER TABLE TB_USUARIO
    ADD DATA_CADASTRO TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
