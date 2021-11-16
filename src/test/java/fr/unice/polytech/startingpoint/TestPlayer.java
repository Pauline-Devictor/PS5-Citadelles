package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayer {
    Player p;
    Building b;

    @BeforeEach
    void setUp(){
        p = new Player();
        b = new Building(5);
        p.build(b);
    }

    @Test
    void buildGold(){
        assertEquals(p.getGold(), 995);
    }
    @Test
    void buildBuilt(){
        assertTrue(b.getBuilt());
    }

    @Test
    void testToString(){
        Deck d = new Deck();
        Player j = new Player();
        j.play(d);
        j.play(d);
        System.out.println(j);
    }
}
