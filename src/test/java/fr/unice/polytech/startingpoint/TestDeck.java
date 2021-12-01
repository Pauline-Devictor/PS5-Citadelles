package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestDeck {
    Deck d;

    @BeforeEach
    void setUp(){
        d = spy(new Deck());
    }

    @Test
    void drawADeckTest(){
        while (!d.isEmpty())
            d.drawACard();
        assertEquals(Optional.empty(),d.drawACard());
    }
    @Test
    void drawADeck(){
        while (!d.isEmpty())
            d.drawACard();
        verify(d,times(65)).drawACard();
    }


}
