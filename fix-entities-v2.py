#!/usr/bin/env python3
import os
import re

MODEL_PATH = "src/main/java/br/com/fiap/mottooth/model"

def fix_entity_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    original = content

    # Remover @SequenceGenerator (pode ter múltiplas linhas)
    content = re.sub(
        r'@SequenceGenerator\s*\([^)]*\)\s*\n?',
        '',
        content,
        flags=re.MULTILINE | re.DOTALL
    )

    # Substituir GenerationType.SEQUENCE por IDENTITY
    content = content.replace('GenerationType.SEQUENCE', 'GenerationType.IDENTITY')

    # Remover parâmetro generator
    content = re.sub(
        r'@GeneratedValue\(strategy = GenerationType\.IDENTITY, generator = "[^"]*"\)',
        '@GeneratedValue(strategy = GenerationType.IDENTITY)',
        content
    )

    if content != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def main():
    print("🔧 Corrigindo entidades JPA...\n")

    fixed = 0
    for filename in os.listdir(MODEL_PATH):
        if filename.endswith('.java'):
            filepath = os.path.join(MODEL_PATH, filename)
            if fix_entity_file(filepath):
                print(f"✓ {filename}")
                fixed += 1

    print(f"\n✅ {fixed} arquivos corrigidos!")
    print("\nTestando compilação...")
    os.system("mvn clean compile")

if __name__ == "__main__":
    main()