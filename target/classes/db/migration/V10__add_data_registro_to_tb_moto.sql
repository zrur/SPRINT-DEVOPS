-- Verifica se a coluna DATA_REGISTRO existe e adiciona se não existir
IF COL_LENGTH('TB_MOTO', 'DATA_REGISTRO') IS NULL
    BEGIN
        ALTER TABLE TB_MOTO
            ADD DATA_REGISTRO DATETIME NULL;
    END
GO

-- Atualiza valores nulos existentes
UPDATE TB_MOTO
SET DATA_REGISTRO = GETDATE()
WHERE DATA_REGISTRO IS NULL;
GO

-- Altera para NOT NULL após preencher as linhas existentes
ALTER TABLE TB_MOTO
    ALTER COLUMN DATA_REGISTRO DATETIME NOT NULL;
GO
