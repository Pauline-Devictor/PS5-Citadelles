package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestGame {
    Game g;
    Player p;
    Player p1;
    Player winner;
    Player tie;
    Player looser;

    @BeforeEach
    void setUp() {
        g = spy(new Game(6));
        p = mock(Player.class);
        p1 = mock(Player.class);
        LOGGER.setLevel(Level.OFF);

        winner = mock(Player.class);
        tie = mock(Player.class);
        looser = mock(Player.class);

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

    @Test
    void run1000Test() {
        g.run1000();
        verify(g, times(1000)).initBoard(anyInt());
    }

    @Test
    void calculStatsPlayerTie() {
        when(winner.getScore()).thenReturn(6);
        when(tie.getScore()).thenReturn(6);
        when(looser.getScore()).thenReturn(3);
        when(g.getPlayers()).thenReturn(List.of(winner, tie, looser));
        assertArrayEquals(g.calculStatsPlayer(new int[4], tie), new int[]{6, 0, 1, 1});
    }

    @Test
    void calculStatsPlayerWin() {
        when(winner.getScore()).thenReturn(7);
        when(tie.getScore()).thenReturn(6);
        when(looser.getScore()).thenReturn(3);
        when(g.getPlayers()).thenReturn(List.of(winner, tie, looser));
        assertArrayEquals(g.calculStatsPlayer(new int[4], winner), new int[]{7, 1, 0, 1});
    }

    @Test
    void calculStatsPlayerLoose() {
        when(winner.getScore()).thenReturn(7);
        when(tie.getScore()).thenReturn(6);
        when(looser.getScore()).thenReturn(3);
        when(g.getPlayers()).thenReturn(List.of(winner, tie, looser));
        assertArrayEquals(new int[]{3, 0, 0, 1}, g.calculStatsPlayer(new int[4], looser));
    }

    @Test
    void calculStatsPlayerLooseTie() {
        when(winner.getScore()).thenReturn(6);
        when(tie.getScore()).thenReturn(6);
        when(looser.getScore()).thenReturn(3);
        when(g.getPlayers()).thenReturn(List.of(winner, tie, looser));
        assertArrayEquals(new int[]{3, 0, 0, 1}, g.calculStatsPlayer(new int[4], looser));
    }

    @Test
    void calculStatsPlayerSum() {
        when(winner.getScore()).thenReturn(6);
        when(tie.getScore()).thenReturn(6);
        when(looser.getScore()).thenReturn(3);
        when(g.getPlayers()).thenReturn(List.of(winner, tie, looser));
        assertArrayEquals(new int[]{15, 1, 0, 2}, g.calculStatsPlayer(new int[]{12, 1, 0, 1}, looser));
    }

    @Test
    void calculStats() {
        when(winner.getScore()).thenReturn(6);
        when(winner.getName()).thenReturn("W");
        when(tie.getScore()).thenReturn(5);
        when(tie.getName()).thenReturn("T");
        when(looser.getScore()).thenReturn(3);
        when(looser.getName()).thenReturn("L");
        when(g.getPlayers()).thenReturn(List.of(winner, tie, looser));
        Map<String, int[]> results = Map.ofEntries(
                Map.entry("W", new int[]{6, 1, 0, 1}),
                Map.entry("T", new int[]{5, 0, 0, 1}),
                Map.entry("L", new int[]{3, 0, 0, 1})
        );
        Map<String, int[]> stats = g.calculStats(new TreeMap<>());
        int[] resultsW = stats.get("W");
        int[] resultsT = stats.get("T");
        int[] resultsL = stats.get("L");
        assertTrue(
                Arrays.equals(resultsW, results.get("W")) &&
                        Arrays.equals(resultsT, results.get("T")) &&
                        Arrays.equals(resultsL, results.get("L"))
        );
    }

}