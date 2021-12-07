package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {
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
        verify(g, times(1)).showEndOfGame();
    }

    @Test
    void runEndOfGame(){
        g.newGame();
        List<Player> players = g.getPlayers();
        int count = 0 ;
        for (Player p: players) {
            if(p.getCity().size()>=8)
                count++;
        }
        assertTrue(count>0 ||
                (g.getBoard().getPile().isEmpty() &&
                        g.getBoard().getBank().getGold()==0) );
    }

    //@Test
    void orderPLayer() {
        when(p.getGoldScore()).thenReturn(15);
        when(p1.getGoldScore()).thenReturn(13);
        when(g.getPlayers()).thenReturn(List.of(p, p1));
        //TODO cf signature
    }
}