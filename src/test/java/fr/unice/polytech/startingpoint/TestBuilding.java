package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBuilding {

    /*@Test
    void testGetCount(){
        Building b = new Building(3);
        assertEquals(3, b.getCost());
    }

    @Test
    void testIsConstructible(){
        Building b = new Building(3);
        assertTrue(b.isBuildable(4));
    }

    @Test
    void testIsConstructible2(){
        Building b = new Building(3);
        assertFalse(b.isBuildable(2));
    }*/

    @Test
    void testToString(){
        Building b = new Building(3);
        assertEquals("[nom], Cout : 3, Construit : false", b.toString());
    }

    @Test
    void isBuildableTestHigher(){
        assertTrue(new Building(2).isBuildable(3));
    }

    @Test
    void isBuildableTesEqual(){
        assertTrue(new Building(2).isBuildable(2));
    }


    @Test
    void isBuildableTestLower(){
        assertFalse(new Building(2).isBuildable(1));
    }
    /*@Test
    void testSetConstruit(){
        Building b = new Building(3);
        b.setBuilt(true);
        assertTrue(b.getBuilt());
    }

    @Test
    void testSetConstruit2(){
        Building b = new Building(3);
        b.setBuilt(false);
        assertFalse(b.getBuilt());
    }

    @Test
    void testGetConstruit(){
        Building b = new Building(3);
        assertFalse(b.getBuilt());

    }*/
}
