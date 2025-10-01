-- V9__add_data_registro_to_tb_moto.sql

-- adiciona a coluna se ainda não existir
DECLARE
e_exists EXCEPTION;
  PRAGMA EXCEPTION_INIT(e_exists, -1430); -- ORA-01430: column being added already exists
BEGIN
EXECUTE IMMEDIATE 'ALTER TABLE TB_MOTO ADD (DATA_REGISTRO TIMESTAMP)';
EXCEPTION
  WHEN e_exists THEN NULL;
END;
/
-- preenche linhas antigas (se houver)
UPDATE TB_MOTO
SET DATA_REGISTRO = SYSTIMESTAMP
WHERE DATA_REGISTRO IS NULL;

-- garante NOT NULL (opcional, mas recomendado)
ALTER TABLE TB_MOTO MODIFY (DATA_REGISTRO TIMESTAMP NOT NULL);
