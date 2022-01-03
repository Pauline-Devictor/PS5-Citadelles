package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestGame {
    Game g;
    Player p;
    Player p1;

    @BeforeEach
    void setUp() {
        g = spy(new Game(6));
        p = mock(Player.class);
        p1 = mock(Player.class);
    }

    @Test
    void runTest() {
        g.run();
        verify(g, times(1)).endOfGame();
    }

    @Test
    void runEndOfGame(){
        g.newGame();
        List<Player> players = g.getPlayers();
        int count = 0;
        for (Player p : players) {
            if (p.getCity().size() >= 8)
                count++;
        }
        assertTrue(count > 0 ||
                (g.getBoard().getPile().isEmpty() &&
                        g.getBoard().getBank().getGold() == 0));
    }

    @Test
    void getEmptyOrderPlayer() {
        List<Player> players = new ArrayList<>();
        when(g.getOrderPlayers()).thenReturn(players);
        g.getOrderPlayer();
        assertEquals(new ArrayList<>(), players);
    }

}