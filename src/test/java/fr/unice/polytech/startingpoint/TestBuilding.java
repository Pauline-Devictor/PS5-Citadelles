package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TestBuilding {
    Building eglise;

    @BeforeEach
    void setUp(){
        eglise = new Building(BuildingEnum.Eglise);
    }
        @Test
    void testToString(){
        assertEquals("Eglise, Cout : 2, Quartier : Religion", eglise.toString());
    }

}
