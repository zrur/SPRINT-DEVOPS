DECLARE
v_missing NUMBER;

  PROCEDURE add_col(p_sql VARCHAR2) IS
BEGIN
EXECUTE IMMEDIATE p_sql;
EXCEPTION
    WHEN OTHERS THEN
      -- Se já existir, ignora; se for outro erro, propaga
      IF SQLCODE NOT IN (-1430) THEN RAISE; END IF; -- ORA-01430: column being added already exists
END;
BEGIN
SELECT COUNT(*) INTO v_missing
FROM user_tab_columns
WHERE table_name = 'TB_CLIENTE'
  AND column_name = 'CPF';
IF v_missing = 0 THEN
    add_col('ALTER TABLE TB_CLIENTE ADD (CPF VARCHAR2(14))');
END IF;

SELECT COUNT(*) INTO v_missing
FROM user_tab_columns
WHERE table_name = 'TB_CLIENTE'
  AND column_name = 'DATA_CADASTRO';
IF v_missing = 0 THEN
    add_col('ALTER TABLE TB_CLIENTE ADD (DATA_CADASTRO TIMESTAMP)');
END IF;

SELECT COUNT(*) INTO v_missing
FROM user_tab_columns
WHERE table_name = 'TB_CLIENTE'
  AND column_name = 'EMAIL';
IF v_missing = 0 THEN
    add_col('ALTER TABLE TB_CLIENTE ADD (EMAIL VARCHAR2(120))');
END IF;

SELECT COUNT(*) INTO v_missing
FROM user_tab_columns
WHERE table_name = 'TB_CLIENTE'
  AND column_name = 'TELEFONE';
IF v_missing = 0 THEN
    add_col('ALTER TABLE TB_CLIENTE ADD (TELEFONE VARCHAR2(20))');
END IF;
END;
/
