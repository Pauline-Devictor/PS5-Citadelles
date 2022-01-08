package fr.unice.polytech.startingpoint.charactersTest;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.Deck;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.characters.Magician;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestMagician {
    Board board;
    Player player;
    Player magician;
    Magician magicianCharacter;

    @BeforeEach
    void setUp() {
        //LOGGER.setLevel(Level.OFF);
        board = spy(new Board());
        player = spy(new Player(board));
        magician = spy(new Player(board));
        magicianCharacter = spy(Magician.class);
    }

    @Test
    void swapHandsDeck() {
        when(player.getRole()).thenReturn(magicianCharacter);
        Deck deck = mock(Deck.class);
        when(board.getPile()).thenReturn(deck);
        //noinspection unchecked
        when(deck.drawACard()).thenReturn(
                Optional.of(new Building(BuildingEnum.Caserne)),
                Optional.of(new Building(BuildingEnum.Laboratoire)),
                Optional.of(new Building(BuildingEnum.Cathedrale)),
                Optional.of(new Building(BuildingEnum.Palais)));

        magicianCharacter.swapHandDeck(player);
        List<Building> cards = List.of(
                new Building(BuildingEnum.Palais),
                new Building(BuildingEnum.Cathedrale),
                new Building(BuildingEnum.Laboratoire),
                new Building(BuildingEnum.Caserne)
        );
        assertEquals(cards, player.getCardHand());
    }

    @Test
    void magicienChooseTarget() {
        when(player.getRole()).thenReturn(magicianCharacter);
        when(board.getPlayers()).thenReturn(List.of(player, magician));
        assertTrue(magicianCharacter.chooseTarget(board, player).isPresent());
    }

    @Test
    void magicianNoTarget() {
        when(player.getRole()).thenReturn(magicianCharacter);
        when(board.getPlayers()).thenReturn(List.of(player));
        assertTrue(magicianCharacter.chooseTarget(board, player).isEmpty());
    }

    @Test
    void swapHandPlayer() {
        magician.drawAndChoose(2, 2);
        player.drawAndChoose(2, 2);
        List<Building> buildMage = magician.getCardHand();
        when(player.getRole()).thenReturn(magicianCharacter);
        when(board.getPlayers()).thenReturn(List.of(player, magician));
        magicianCharacter.usePower(board);
        assertSame(buildMage, player.getCardHand());
    }


    @Test
    void swapEmptyHandPlayer() {
        player.drawAndChoose(2, 2);
        List<Building> buildMage = magician.getCardHand();
        when(player.getRole()).thenReturn(magicianCharacter);
        when(board.getPlayers()).thenReturn(List.of(player, magician));
        magicianCharacter.usePower(board);
        assertSame(buildMage, player.getCardHand());
    }
}
