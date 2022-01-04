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

public class TestLaboratory {
    Building eglise;
    Player p;

    @BeforeEach
    void setUp() {
        LOGGER.setLevel(Level.OFF);
        p = spy(new Player(new Board()));
        eglise = new Building(BuildingEnum.Eglise);
    }

    @Test
    void testLaboratory() {
        when(p.getGold()).thenReturn(25);
        when(p.getCity()).thenReturn(List.of(new Laboratory()));
        int cardHand = p.getCardHand().size();
        p.cityEffects();
        assertEquals(cardHand - 1, p.getCardHand().size());
    }

    @Test
    void testLaboratoryEmptyHand() {
        when(p.getGold()).thenReturn(25);
        when(p.getCity()).thenReturn(List.of(new Laboratory()));
        while (p.getCardHand().size() > 0)
            p.discardCard();
        p.cityEffects();
        assertEquals(0, p.getCardHand().size());
    }
}
