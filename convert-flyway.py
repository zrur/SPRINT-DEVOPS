#!/usr/bin/env python3
import os
import re

MIGRATION_PATH = "src/main/resources/db/migration"

def convert_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Backup
    with open(filepath + '.oracle.bak', 'w', encoding='utf-8') as f:
        f.write(content)
    
    # Conversões
    content = content.replace('VARCHAR2', 'VARCHAR')
    content = re.sub(r'NUMBER\([^\)]*\)', 'DECIMAL', content)
    content = content.replace('NUMBER', 'INT')
    content = content.replace('CLOB', 'VARCHAR(MAX)')
    content = content.replace('TIMESTAMP', 'DATETIME')
    content = content.replace('SYSDATE', 'GETDATE()')
    
    # INTEGER PRIMARY KEY -> INT IDENTITY
    content = re.sub(
        r'(\s+)INTEGER(\s+)PRIMARY KEY',
        r'\1INT IDENTITY(1,1)\2PRIMARY KEY',
        content
    )
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print(f"✓ {os.path.basename(filepath)}")

def main():
    if not os.path.exists(MIGRATION_PATH):
        print(f"❌ {MIGRATION_PATH} não encontrado!")
        return
    
    print("🔧 Convertendo scripts Flyway Oracle -> SQL Server...\n")
    
    for filename in sorted(os.listdir(MIGRATION_PATH)):
        if filename.endswith('.sql'):
            filepath = os.path.join(MIGRATION_PATH, filename)
            convert_file(filepath)
    
    print("\n✅ Conversão concluída!")

if __name__ == "__main__":
    main()
