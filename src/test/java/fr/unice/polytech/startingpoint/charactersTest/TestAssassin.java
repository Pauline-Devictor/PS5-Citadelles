package fr.unice.polytech.startingpoint.charactersTest;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.*;
import fr.unice.polytech.startingpoint.characters.Assassin;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.Magician;
import fr.unice.polytech.startingpoint.characters.Merchant;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestAssassin {
    Board board;
    Player player;
    Player magician;
    Assassin assassinCharacter;
    Magician magicianCharacter;
    Merchant merchantCharacter;

    @BeforeEach
    void setUp() {
        //LOGGER.setLevel(Level.OFF);
        board = spy(new Board());
        player = spy(new Player(board));
        magician = spy(new Player(board));
        assassinCharacter = spy(Assassin.class);
        magicianCharacter = spy(Magician.class);
        merchantCharacter = spy(Merchant.class);
    }

    @Test
    void killSomeone() {
        when(player.getRole()).thenReturn(assassinCharacter);
        when(board.getPlayers()).thenReturn(List.of(player));
        assassinCharacter.usePower(board);
        int killed = (int) board.getCharacters().stream().filter(Character::isMurdered).count();
        assertEquals(1, killed);
    }

    @Test
    void assassinChoosesCondoBecauseOwns1CostBuilding() {
        when(player.getCity()).thenReturn(List.of(new Building(BuildingEnum.Manoir),
                new Building(BuildingEnum.TourDeGuet),
                new Building(BuildingEnum.Port),
                new Building(BuildingEnum.Port),
                new Building(BuildingEnum.Port),
                new Building(BuildingEnum.Chateau)));
        when(player.getRole()).thenReturn(assassinCharacter);
        when(board.getPlayers()).thenReturn(List.of(player));
        when(player.getMajority()).thenReturn(District.Commercial);
        assassinCharacter.usePower(board);
        assertTrue(board.getCharactersInfos(7).isMurdered());
    }

    @Test
    void assassinChooseVictimColorMerchant() {
        when(magician.getCity()).thenReturn(List.of(
                new Building(BuildingEnum.Manoir),
                new Building(BuildingEnum.Prison),
                new Building(BuildingEnum.Echoppe),
                new Building(BuildingEnum.Port),
                new Building(BuildingEnum.Marche),
                new Building(BuildingEnum.Comptoir)));
        when(player.getRole()).thenReturn(assassinCharacter);
        when(magician.getRole()).thenReturn(merchantCharacter);
        when(board.getPlayers()).thenReturn(List.of(player, magician));
        when(player.getMajority()).thenReturn(District.Commercial);
        assassinCharacter.usePower(board);
        assertTrue(board.getCharactersInfos(4).isMurdered());
    }

    @Test
    void assassinChooseThiefPotentielBigTheft() {
        when(magician.getGold()).thenReturn(5);
        when(player.getGold()).thenReturn(7);
        when(player.getRole()).thenReturn(assassinCharacter);
        when(magician.getRole()).thenReturn(magicianCharacter);
        when(board.getPlayers()).thenReturn(List.of(player, magician));
        assassinCharacter.usePower(board);
        assertTrue(board.getCharactersInfos(1).isMurdered());
    }

    @Test
    void assassinChooseVictimMagician() {
        player.drawAndChoose(4, 4);
        when(player.getRole()).thenReturn(assassinCharacter);
        when(board.getPlayers()).thenReturn(List.of(player));
        assassinCharacter.usePower(board);
        assertTrue(board.getCharactersInfos(2).isMurdered());
    }

    @Test
    void assassinArchi() {
        when(magician.getCity()).thenReturn(List.of(new Manufactory(), new Graveyard(), new Library(), new Observatory(), new MiracleCourtyard()));
        when(magician.getCardHand()).thenReturn(List.of(new MiracleCourtyard(), new Graveyard()));
        when(magician.getGold()).thenReturn(5);
        when(player.getRole()).thenReturn(assassinCharacter);
        when(board.getPlayers()).thenReturn(List.of(magician, player));
        assassinCharacter.usePower(board);
        //archi
        assertTrue(board.getCharactersInfos(6).isMurdered());
    }

    @Test
    void assassinTargetKing() {
        when(magician.getCity()).thenReturn(List.of(new Manufactory(), new Graveyard(), new Library(), new Observatory(), new MiracleCourtyard()));
        when(magician.getCardHand()).thenReturn(List.of(new MiracleCourtyard(), new Graveyard()));
        when(magician.getGold()).thenReturn(5);
        when(player.getRole()).thenReturn(assassinCharacter);
        when(board.getPlayers()).thenReturn(List.of(magician, player));
        // should kill king over anything
        assassinCharacter.setPriorityTarget(3);
        assassinCharacter.usePower(board);
        assertTrue(board.getCharactersInfos(3).isMurdered());
    }
}
