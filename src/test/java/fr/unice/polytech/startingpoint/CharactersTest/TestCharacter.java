package fr.unice.polytech.startingpoint.CharactersTest;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestCharacter {
    Board board;
    Player player;
    Player archi;
    Character archiCharacter;

    @BeforeEach
    void setUp() {
        board = spy(new Board());
        player = spy(new Player(board));
        archi = spy(new Player(board));
        archiCharacter = spy(Architect.class);

    }

    @Test
    void findPlayerEmpty() {
        when(player.getRole()).thenReturn(archiCharacter);
        when(board.getPlayers()).thenReturn(new ArrayList<>());
        assertEquals(Optional.empty(), archiCharacter.findPlayer(board));
    }

    @Test
    void findPlayerMatchingRole() {
        Player a = new Player(board);
        Player m = new Player(board);
        Player t = new Player(board);
        player.pickRole(6);
        a.pickRole(0);
        m.pickRole(5);
        t.pickRole(1);
        when(board.getPlayers()).thenReturn(List.of(player, a, m, t));
        assertEquals(Optional.of(player), archiCharacter.findPlayer(board));
    }

    @Test
    void findPlayerAnyRole() {
        Player a = new Player(board);
        Player m = new Player(board);
        Player t = new Player(board);
        player.pickRole(6);
        a.pickRole(0);
        m.pickRole(5);
        t.pickRole(1);
        when(board.getPlayers()).thenReturn(List.of(a, m, t));
        assertEquals(Optional.empty(), archiCharacter.findPlayer(board));
    }

}
