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
        b = new Building(2);
        bNeg = new Building(-2);
        bFree = new Building(0);
    }

    @Test
    void buildGold(){

        p.build(b);
        assertEquals(2,p.getGoldScore());
    }
    @Test
    void buildBuilt(){

        p.build(b);
        assertTrue(b.getBuilt());
    }

    @Test
    void buildScore(){
        p.build(b);
        assertEquals(2,p.getGoldScore());
    }
    @Test
    void buildScore0(){
        p.build(bFree);
        assertEquals(0,p.getGoldScore());
    }
    @Test
    void buildScoreNeg(){
        p.build(bNeg);
        assertEquals(2,p.getGoldScore());
    }
    @Test
    void hasRole(){
        p.chooseRole();
        assertNotNull(p.getRole());
    }
}
