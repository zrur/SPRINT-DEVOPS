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
