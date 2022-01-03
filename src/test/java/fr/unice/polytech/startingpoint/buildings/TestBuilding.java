package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestBuilding {
    Building eglise;
    Player p;

    @BeforeEach
    void setUp() {
        p = spy(new Player(new Board()));
        eglise = new Building(BuildingEnum.Eglise);
    }

    @Test
    void testToString() {
        assertEquals("Eglise, Cout : 2, Quartier : Religion", eglise.toString());
    }

}
