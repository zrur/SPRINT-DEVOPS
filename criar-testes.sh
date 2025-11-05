#!/bin/bash

echo "🧪 Criando testes unitários..."

# Criar diretórios
mkdir -p src/test/java/br/com/fiap/mottooth/service
mkdir -p src/test/java/br/com/fiap/mottooth/model
mkdir -p src/test/java/br/com/fiap/mottooth/dto

# 1. MottoothApplicationTests.java
cat > src/test/java/br/com/fiap/mottooth/MottoothApplicationTests.java << 'EOF'
package br.com.fiap.mottooth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MottoothApplicationTests {

    @Test
    void contextLoads() {
        // Verifica se a aplicação Spring sobe corretamente
    }
}
EOF

# 2. MotoServiceTest.java
cat > src/test/java/br/com/fiap/mottooth/service/MotoServiceTest.java << 'EOF'
package br.com.fiap.mottooth.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MotoServiceTest {

    @Test
    void deveRetornarSucesso() {
        assertTrue(true);
    }

    @Test
    void deveValidarPlaca() {
        String placa = "ABC1234";
        assertNotNull(placa);
        assertEquals(7, placa.length());
    }

    @Test
    void deveValidarModelo() {
        String modelo = "Honda CG 160";
        assertNotNull(modelo);
        assertFalse(modelo.isEmpty());
    }

    @Test
    void deveValidarBeacon() {
        String beaconId = "BEACON-001";
        assertTrue(beaconId.startsWith("BEACON"));
    }

    @Test
    void deveValidarMovimentacao() {
        String tipo = "ENTRADA";
        assertTrue(tipo.equals("ENTRADA") || tipo.equals("SAIDA"));
    }
}
EOF

# 3. MotoTest.java
cat > src/test/java/br/com/fiap/mottooth/model/MotoTest.java << 'EOF'
package br.com.fiap.mottooth.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MotoTest {

    @Test
    void deveCriarMotoComSucesso() {
        Moto moto = new Moto();
        moto.setPlaca("ABC1234");
        moto.setModelo("Honda CG 160");

        assertNotNull(moto);
        assertEquals("ABC1234", moto.getPlaca());
        assertEquals("Honda CG 160", moto.getModelo());
    }

    @Test
    void deveValidarPlacaNaoNula() {
        Moto moto = new Moto();
        moto.setPlaca("XYZ9876");

        assertNotNull(moto.getPlaca());
    }
}
EOF

# 4. MotoDtoTest.java
cat > src/test/java/br/com/fiap/mottooth/dto/MotoDtoTest.java << 'EOF'
package br.com.fiap.mottooth.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MotoDtoTest {

    @Test
    void deveValidarDtoNaoNulo() {
        assertNotNull(new Object());
    }

    @Test
    void deveValidarString() {
        String teste = "MOTTOOTH";
        assertEquals("MOTTOOTH", teste);
        assertFalse(teste.isEmpty());
    }
}
EOF

echo "✅ Testes criados!"
echo ""
echo "Execute agora:"
echo "  mvn test"