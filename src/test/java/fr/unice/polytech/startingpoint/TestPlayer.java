package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TestPlayer {
    Player p;
    Building b;
    Building bNeg;
    Building bFree;
    Board board;

    @BeforeEach
    void setUp(){
        board = new Board();
        p = new Player(board);
        b = new Building(5);
        bNeg = new Building(0);
        bFree = new Building(-2);
    }

    @Test
    void buildGold(){

        p.build(b);
        assertEquals(2,p.getGold());
    }
    @Test
    void buildBuilt(){

        p.build(b);
        assertTrue(b.getBuilt());
    }

    @Test
    void buildScore(){
        p.build(b);
        assertEquals(5,p.getGoldScore());
    }
    void buildScore0(){
        assertEquals(0,p.getGoldScore());
    }
    void buildScoreNeg(){
        assertEquals(2,p.getGoldScore());
    }

}
