package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

public class TestBuilding {
    Building eglise;
    Player p;

    @BeforeEach
    void setUp() {
        LOGGER.setLevel(Level.OFF);
        p = spy(new Player(new Board()));
        eglise = new Building(BuildingEnum.Eglise);
    }

    @Test
    void testToString() {
        assertEquals("Eglise, Cout : 2, Quartier : Religion", eglise.toString());
    }

}
