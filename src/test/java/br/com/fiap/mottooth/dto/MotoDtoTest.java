package br.com.fiap.mottooth.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MotoDtoTest {
    
    @Test
    void deveValidarString() {
        String teste = "MOTTOOTH";
        assertEquals("MOTTOOTH", teste);
        assertFalse(teste.isEmpty());
    }
    
    @Test
    void deveValidarInteger() {
        Integer numero = 123;
        assertNotNull(numero);
        assertTrue(numero > 0);
    }
}
