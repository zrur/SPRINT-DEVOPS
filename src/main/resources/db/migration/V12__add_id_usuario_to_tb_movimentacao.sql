-- V12__add_id_usuario_to_tb_movimentacao.sql

-- Adiciona a coluna (permite null) se ainda não existir
IF COL_LENGTH('TB_MOVIMENTACAO', 'ID_USUARIO') IS NULL
BEGIN
ALTER TABLE TB_MOVIMENTACAO ADD ID_USUARIO INT NULL;
END;
GO

-- Cria a FK se a tabela TB_USUARIO existir e a constraint ainda não existir
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'TB_USUARIO')
BEGIN
    IF NOT EXISTS (
        SELECT * FROM sys.foreign_keys
        WHERE name = 'FK_MOV_USUARIO'
          AND parent_object_id = OBJECT_ID('TB_MOVIMENTACAO')
    )
BEGIN
ALTER TABLE TB_MOVIMENTACAO
    ADD CONSTRAINT FK_MOV_USUARIO
        FOREIGN KEY (ID_USUARIO)
            REFERENCES TB_USUARIO (ID_USUARIO);
END;
END;
GO
