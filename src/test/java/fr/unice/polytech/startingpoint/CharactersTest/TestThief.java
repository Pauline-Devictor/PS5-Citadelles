package fr.unice.polytech.startingpoint.CharactersTest;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestThief {
    Board board;
    Player player;
    Character thiefCharacter;

    @BeforeEach
    void setUp() {
        board = spy(new Board());
        player = spy(new Player(board));
        thiefCharacter = spy(Thief.class);

    }

    @Test
    void stealSomeone() {
        when(player.getRole()).thenReturn(thiefCharacter);
        when(board.getPlayers()).thenReturn(List.of(player));
        thiefCharacter.usePower(board);
        int stole = (int) board.getCharacters().stream().filter(Character::isStolen).count();
        assertEquals(1, stole);
    }
}
