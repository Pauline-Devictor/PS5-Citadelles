package fr.unice.polytech.startingpoint.charactersTest;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestArchitect {
    Board board;
    Character king;
    Player player;
    Player archi;
    Player bishop;
    Player condo;
    Player merchant;
    Player magician;
    Character archiCharacter;
    Assassin assassinCharacter;
    Character thiefCharacter;
    Character condottiereCharacter;
    Magician magicianCharacter;
    Bishop bishopCharacter;
    Character merchantCharacter;

    @BeforeEach
    void setUp() {
        //LOGGER.setLevel(Level.OFF);
        board = spy(new Board());
        king = new King();
        player = spy(new Player(board));
        archi = spy(new Player(board));
        condo = spy(new Player(board));
        bishop = spy(new Player(board));
        merchant = spy(new Player(board));
        magician = spy(new Player(board));
        archiCharacter = spy(Architect.class);
        assassinCharacter = spy(Assassin.class);
        thiefCharacter = spy(Thief.class);
        magicianCharacter = spy(Magician.class);
        condottiereCharacter = spy(Condottiere.class);
        bishopCharacter = spy(Bishop.class);
        merchantCharacter = spy(Merchant.class);
    }

    @Test
    void buildArchitect() {
        when(player.getRole()).thenReturn(archiCharacter);
        when(board.getPlayers()).thenReturn(List.of(player));
        archiCharacter.usePower(board);
        assertEquals(3, player.getNbBuildable());
    }

    @Test
    void architectPower() {
        when(player.getRole()).thenReturn(archiCharacter);
        when(board.getPlayers()).thenReturn(List.of(player));
        when(board.getCharacters()).thenReturn(List.of(archiCharacter));
        archiCharacter.usePower(board);
        assertEquals(3, player.getNbBuildable());
    }

}
