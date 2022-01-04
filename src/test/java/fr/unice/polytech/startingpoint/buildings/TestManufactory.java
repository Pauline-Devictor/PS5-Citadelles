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

public class TestManufactory {
    Player p;

    @BeforeEach
    void setUp() {
        LOGGER.setLevel(Level.OFF);
        p = spy(new Player(new Board()));
    }


    @Test
    void testManufacture() {
        when(p.getGold()).thenReturn(25);
        when(p.getCity()).thenReturn(List.of(new Manufactory()));
        int cardHand = p.getCardHand().size();
        p.cityEffects();
        assertEquals(3 + cardHand, p.getCardHand().size());
    }

    @Test
    void testManufactureNoGold() {
        Player p = spy(new Player(new Board()));
        when(p.getGold()).thenReturn(0);
        when(p.getCity()).thenReturn(List.of(new Manufactory()));
        int cardHand = p.getCardHand().size();
        p.cityEffects();
        assertEquals(cardHand, p.getCardHand().size());
    }
}
