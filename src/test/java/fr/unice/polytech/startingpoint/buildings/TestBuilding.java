package fr.unice.polytech.startingpoint.buildings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBuilding {
    Building eglise;

    @BeforeEach
    void setUp() {
        LOGGER.setLevel(Level.OFF);
        eglise = new Building(BuildingEnum.Eglise);
    }

    @Test
    void testToString() {
        assertEquals("Eglise, Cout : 2, Quartier : Religion", eglise.toString());
    }

}
