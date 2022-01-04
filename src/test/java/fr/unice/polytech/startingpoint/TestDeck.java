package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Library;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestDeck {
    Deck d;

    @BeforeEach
    void setUp(){
        LOGGER.setLevel(Level.OFF);
        d = spy(new Deck());
    }

    @Test
    void drawAEmptyDeck() {
        while (!d.isEmpty())
            d.drawACard();
        assertEquals(Optional.empty(), d.drawACard());
    }

    @Test
    void drawADeck() {
        while (!d.isEmpty())
            d.drawACard();
        verify(d, times(65)).drawACard();
    }

    @Test
    void putCard() {
        int size = d.numberOfCards();
        d.putCard(new Library());
        assertEquals(1, d.numberOfCards() - size);
    }

    @Test
    void initDeck() {
        assertEquals(65, d.numberOfCards());
    }
}
