-- V9__add_data_registro_to_tb_moto.sql (SQL Server Compatível)

-- Adiciona a coluna DATA_REGISTRO se ainda não existir
IF COL_LENGTH('TB_MOTO', 'DATA_REGISTRO') IS NULL
BEGIN
ALTER TABLE TB_MOTO ADD DATA_REGISTRO DATETIME NULL;
END;
GO

-- Preenche as linhas existentes com a data atual
UPDATE TB_MOTO
SET DATA_REGISTRO = GETDATE()
WHERE DATA_REGISTRO IS NULL;
GO

-- Garante que a coluna não aceite valores nulos
ALTER TABLE TB_MOTO
ALTER COLUMN DATA_REGISTRO DATETIME NOT NULL;
GO
