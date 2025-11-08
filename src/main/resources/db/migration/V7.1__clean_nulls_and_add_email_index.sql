-- V7.1__clean_nulls_and_add_email_index.sql

-- Verifica se índice já existe e remove
IF EXISTS (
    SELECT * FROM sys.indexes
    WHERE name = 'UX_CLIENTE_EMAIL' AND object_id = OBJECT_ID('TB_CLIENTE')
)
    BEGIN
        DROP INDEX UX_CLIENTE_EMAIL ON TB_CLIENTE;
    END;

-- Cria índice único apenas se não houver duplicados nem nulos
IF NOT EXISTS (
    SELECT EMAIL
    FROM TB_CLIENTE
    GROUP BY EMAIL
    HAVING COUNT(*) > 1 OR EMAIL IS NULL
)
    BEGIN
        CREATE UNIQUE INDEX UX_CLIENTE_EMAIL ON TB_CLIENTE (EMAIL);
    END
ELSE
    BEGIN
        PRINT 'Não foi possível criar índice UX_CLIENTE_EMAIL: há EMAILs nulos ou duplicados.';
    END;
