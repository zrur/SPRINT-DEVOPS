-- V7__cliente_unique_and_checks.sql

-- Verifica se índice já existe
IF EXISTS (SELECT * FROM sys.indexes WHERE name = 'UQ_CLIENTE_CPF' AND object_id = OBJECT_ID('TB_CLIENTE'))
BEGIN
DROP INDEX UQ_CLIENTE_CPF ON TB_CLIENTE;
END;

-- Tenta criar o índice, mas apenas se não houver duplicados ou NULLs
IF NOT EXISTS (
    SELECT CPF
    FROM TB_CLIENTE
    GROUP BY CPF
    HAVING COUNT(*) > 1 OR CPF IS NULL
)
BEGIN
CREATE UNIQUE INDEX UQ_CLIENTE_CPF ON TB_CLIENTE (CPF);
END
ELSE
BEGIN
    PRINT 'Não foi possível criar índice único em CPF: há NULLs ou CPFs duplicados na tabela TB_CLIENTE.';
END;
