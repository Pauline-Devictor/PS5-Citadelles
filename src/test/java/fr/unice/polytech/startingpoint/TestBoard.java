package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void roleFree(){
        board.getCharactersInfos(3).took();
        board.release();
        assertTrue(board.getCharactersInfos(3).isAvailable());
    }


}
