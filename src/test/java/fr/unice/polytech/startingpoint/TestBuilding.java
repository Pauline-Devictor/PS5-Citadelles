package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBuilding {

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
}
