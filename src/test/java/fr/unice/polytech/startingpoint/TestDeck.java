package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeck {

    @Test
    void drawACardTest(){
        assertTrue(new Deck().drawACard() instanceof Building);
    }

}
