package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBatiment {

    @Test
    void testGetCount(){
        Batiment b = new Batiment(3);
        assertEquals(3, b.getCout());
    }

    @Test
    void testIsConstructible(){
        Batiment b = new Batiment(3);
        assertTrue(b.isConstructible(4));
    }

    @Test
    void testIsConstructible2(){
        Batiment b = new Batiment(3);
        assertFalse(b.isConstructible(2));
    }

    @Test
    void testToString(){
        Batiment b = new Batiment(3);
        assertEquals("Batiment : [nom], Cout : 3, Construit : false", b.toString());
    }

    @Test
    void testSetConstruit(){
        Batiment b = new Batiment(3);
        b.setConstruit(true);
        assertTrue(b.getConstruit());
    }

    @Test
    void testSetConstruit2(){
        Batiment b = new Batiment(3);
        b.setConstruit(false);
        assertFalse(b.getConstruit());
    }

    @Test
    void testGetConstruit(){
        Batiment b = new Batiment(3);
        assertFalse(b.getConstruit());

    }
}
