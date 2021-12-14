package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBoard {
    Board board;
    @BeforeEach
    void setup(){
        board = new Board();
    }
    @Test
    void roleFree(){
        board.getCharactersInfos(3).took();
        board.setAllFree();
        assertTrue(board.getCharactersInfos(3).isAvailable());
    }
}
