package br.com.fiap.mottooth.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MotoTest {
    
    @Test
    void deveCriarMotoComSucesso() {
        Moto moto = new Moto();
        moto.setPlaca("ABC1234");
        
        assertNotNull(moto);
        assertEquals("ABC1234", moto.getPlaca());
    }
    
    @Test
    void deveValidarPlacaNaoNula() {
        Moto moto = new Moto();
        moto.setPlaca("XYZ9876");
        
        assertNotNull(moto.getPlaca());
        assertEquals("XYZ9876", moto.getPlaca());
    }
    
    @Test
    void deveValidarIdNulo() {
        Moto moto = new Moto();
        assertNull(moto.getId());
    }
}
