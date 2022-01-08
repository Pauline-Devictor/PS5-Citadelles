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
    Player winner;
    Player tie;
    Player looser;
    Board board;

    @BeforeEach
    void setUp() {
        g = spy(new Game());
        LOGGER.setLevel(Level.OFF);
        winner = mock(Player.class);
        tie = mock(Player.class);
        looser = mock(Player.class);
        board = mock(Board.class);
    }

    @Test
    void runTest() {
        g.run();
        verify(g, times(1)).endOfGame();
    }

    @Test
    void runEndOfGame() {
        g.newGame();
        List<Player> players = g.getBoard().getPlayers();
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
        verify(g, times(1000)).initBoard();
    }

    @Test
    void calculStatsPlayerTie() {
        when(winner.getScore()).thenReturn(6);
        when(tie.getScore()).thenReturn(6);
        when(looser.getScore()).thenReturn(3);
        assertArrayEquals(g.calculStatsPlayer(new int[4], tie, List.of(winner, tie, looser)), new int[]{6, 0, 1, 1});
    }

    @Test
    void calculStatsPlayerWin() {
        when(winner.getScore()).thenReturn(7);
        when(tie.getScore()).thenReturn(6);
        when(looser.getScore()).thenReturn(3);
        assertArrayEquals(g.calculStatsPlayer(new int[4], winner, List.of(winner, tie, looser)), new int[]{7, 1, 0, 1});
    }

    @Test
    void calculStatsPlayerLoose() {
        when(winner.getScore()).thenReturn(7);
        when(tie.getScore()).thenReturn(6);
        when(looser.getScore()).thenReturn(3);
        when(board.getPlayers()).thenReturn(List.of(winner, tie, looser));
        when(g.getBoard()).thenReturn(board);
        assertArrayEquals(new int[]{3, 0, 0, 1}, g.calculStatsPlayer(new int[4], looser, List.of(winner, tie, looser)));
    }

    @Test
    void calculStatsPlayerLooseTie() {
        when(winner.getScore()).thenReturn(6);
        when(tie.getScore()).thenReturn(6);
        when(looser.getScore()).thenReturn(3);
        assertArrayEquals(new int[]{3, 0, 0, 1}, g.calculStatsPlayer(new int[4], looser, List.of(winner, tie, looser)));
    }

    @Test
    void calculStatsPlayerSum() {
        when(winner.getScore()).thenReturn(6);
        when(tie.getScore()).thenReturn(6);
        when(looser.getScore()).thenReturn(3);
        assertArrayEquals(new int[]{15, 1, 0, 2}, g.calculStatsPlayer(new int[]{12, 1, 0, 1}, looser, List.of(winner, tie, looser)));
    }

    @Test
    void calculStats() {
        when(winner.getScore()).thenReturn(6);
        when(winner.getName()).thenReturn("W");
        when(tie.getScore()).thenReturn(5);
        when(tie.getName()).thenReturn("T");
        when(looser.getScore()).thenReturn(3);
        when(looser.getName()).thenReturn("L");
        when(board.getPlayers()).thenReturn(new ArrayList<>(List.of(winner, tie, looser)));
        when(g.getBoard()).thenReturn(board);
        Map<String, int[]> stats = g.calculStats(new TreeMap<>());
        int[] resultsW = new int[]{6, 1, 0, 1};
        int[] resultsT = new int[]{5, 0, 0, 1};
        int[] resultsL = new int[]{3, 0, 0, 1};
        assertTrue(
                Arrays.equals(resultsW, stats.get("W")) &&
                        Arrays.equals(resultsT, stats.get("T")) &&
                        Arrays.equals(resultsL, stats.get("L"))
        );
    }

    @Test
    void calculStatsShuffle() {
        when(winner.getScore()).thenReturn(6);
        when(winner.getName()).thenReturn("W");
        when(tie.getScore()).thenReturn(5);
        when(tie.getName()).thenReturn("T");
        when(looser.getScore()).thenReturn(3);
        when(looser.getName()).thenReturn("L");
        when(board.getPlayers()).thenReturn(new ArrayList<>(List.of(looser, winner, tie)));
        when(g.getBoard()).thenReturn(board);
        Map<String, int[]> stats = g.calculStats(new TreeMap<>());
        int[] resultsW = new int[]{6, 1, 0, 1};
        int[] resultsT = new int[]{5, 0, 0, 1};
        int[] resultsL = new int[]{3, 0, 0, 1};
        assertTrue(
                Arrays.equals(resultsW, stats.get("W")) &&
                        Arrays.equals(resultsT, stats.get("T")) &&
                        Arrays.equals(resultsL, stats.get("L"))
        );
    }

    @Test
    void gameTest2Players() {
        g.run1000("HighScoreThief", "RushArchiLab");
        verify(g, times(1000)).newGame();
    }

}