package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void determineWinner() {
        Game g = spy(new Game(new Board(),2));
        Player p = mock(Player.class);
        Player p1 = mock(Player.class);

        g.determineWinner();
    }

    @Test
    void runTest(){
        Game g = spy(new Game(new Board(),2));
        g.run();
        verify(g,times(1)).determineWinner();
        verify(g,times(1)).showWinner(anyList());
    }


}