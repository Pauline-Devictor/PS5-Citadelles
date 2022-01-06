package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class TestUniversity {
    Player p;

    @BeforeEach
    void setUp() {
        LOGGER.setLevel(Level.OFF);
        p = spy(new Player(new Board()));
    }

    @Test
    void useEffect() {
        when(p.getCity()).thenReturn(List.of(new University()));
        p.cityEffectsEnd();
        assertEquals(2, p.getScore());
    }
}