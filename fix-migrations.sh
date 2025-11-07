#!/bin/bash

echo "Corrigindo arquivos de migration..."

# Caminho para a pasta de migrations (ajuste se estiver diferente)
MIGRATION_DIR="src/main/resources/db/migration"

# 1. Remove barras '/' isoladas no fim de linha (usadas no Oracle)
find "$MIGRATION_DIR" -type f -name "*.sql" -exec sed -i '/^[[:space:]]*\/[[:space:]]*$/d' {} \;

# 2. Substitui 'GETDATE()TIME' por 'GETDATE()'
find "$MIGRATION_DIR" -type f -name "*.sql" -exec sed -i 's/GETDATE()TIME/GETDATE()/g' {} \;

echo "Concluído! Verifique os arquivos alterados com git diff."
