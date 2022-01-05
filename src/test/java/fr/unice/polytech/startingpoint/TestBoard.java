package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.strategies.HighScoreArchi;
import fr.unice.polytech.startingpoint.strategies.Player;
import fr.unice.polytech.startingpoint.strategies.RushArchiLab;
import fr.unice.polytech.startingpoint.strategies.RushMerch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static org.junit.jupiter.api.Assertions.*;

public class TestBoard {
    Board board;
    @BeforeEach
    void setup(){
        LOGGER.setLevel(Level.OFF);
        board = new Board();
    }

    @Test
    void roleFree() {
        board.getCharactersInfos(3).took();
        board.release();
        assertTrue(board.getCharactersInfos(3).isAvailable());
    }

    @Test
    void board() {
        Board b = new Board("Player", "HighScoreArchi", "RushArchiLab", "RushMerch", "Player");
        List tmp = List.of(new Player(b), new HighScoreArchi(b), new RushArchiLab(b), new RushMerch(b), new Player(b));
        tmp = tmp.stream().map(Object::getClass).toList();
        assertEquals(tmp, b.getPlayers().stream().map(Object::getClass).toList());
        assertEquals(5, b.getPlayers().size());
    }

    @Test
    void boardMin() {
        Board b = new Board("Player", "Player");
        assertEquals(2, b.getPlayers().size());
    }

    @Test
    void boardMax() {
        Board b = new Board("Player", "Player", "Player", "Player", "Player", "Player");
        assertEquals(6, b.getPlayers().size());
    }

    @Test
    void board7() {
        Board b = new Board("Player", "Player", "Player", "Player", "Player", "Player", "Player");
        assertEquals(4, b.getPlayers().size());
    }

    @Test
    void boardDefault() {
        Board b = new Board();
        assertEquals(4, b.getPlayers().size());
    }

}
