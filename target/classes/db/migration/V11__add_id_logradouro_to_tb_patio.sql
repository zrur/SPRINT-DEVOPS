-- V11__add_id_logradouro_to_tb_patio.sql
-- Adiciona a coluna ID_LOGRADOURO na TB_PATIO (nullable) e cria FK/índice se não existirem.

-- Adiciona a coluna, se não existir
IF COL_LENGTH('TB_PATIO', 'ID_LOGRADOURO') IS NULL
BEGIN
ALTER TABLE TB_PATIO ADD ID_LOGRADOURO INT NULL;
END;
GO

-- Cria índice, se ainda não existir
IF NOT EXISTS (
    SELECT * FROM sys.indexes
    WHERE name = 'IDX_PATIO_LOGRADOURO'
      AND object_id = OBJECT_ID('TB_PATIO')
)
BEGIN
CREATE INDEX IDX_PATIO_LOGRADOURO ON TB_PATIO (ID_LOGRADOURO);
END;
GO

-- Cria FK se tabela de referência existe e FK ainda não foi criada
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'TB_LOGRADOURO')
BEGIN
    IF NOT EXISTS (
        SELECT * FROM sys.foreign_keys
        WHERE name = 'FK_PATIO_LOGRADOURO'
          AND parent_object_id = OBJECT_ID('TB_PATIO')
    )
BEGIN
ALTER TABLE TB_PATIO
    ADD CONSTRAINT FK_PATIO_LOGRADOURO
        FOREIGN KEY (ID_LOGRADOURO)
            REFERENCES TB_LOGRADOURO (ID_LOGRADOURO);
END;
END;
GO
