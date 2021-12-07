package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestCharacter {
    Board board;
    Character king;
    Player player;
    Player archi;
    Character archiCharacter;
    Character assasinCharacter;
    Character thiefCharacter;
    Magician magicianCharacter;

    @BeforeEach
    void setUp() {
        board = spy(new Board());
        king = new King();
        player = spy(new Player(board));
        archi = spy(new Player(board));
        archiCharacter = spy(Architect.class);
        assasinCharacter = spy(Assassin.class);
        thiefCharacter = spy(Thief.class);
        magicianCharacter = spy(Magician.class);
    }

    @Test
    void setTaxes() {
        when(player.getCity()).thenReturn(List.of(new Building(BuildingEnum.Manoir),
                new Building(BuildingEnum.TourDeGuet),
                new Building(BuildingEnum.Port),
                new Building(BuildingEnum.Chateau)));
        king.collectTaxes(player, District.Noble);
        assertEquals(2, player.getTaxes());
    }

    @Test
    void setTaxesEmpty() {
        when(player.getCity()).thenReturn(new ArrayList<>());
        king.collectTaxes(player, District.Noble);
        assertEquals(0, player.getTaxes());
    }

    @Test
    void killSomeone() {
        player.setRole(Optional.of(assasinCharacter));
        when(board.getPlayers()).thenReturn(List.of(player));
        assasinCharacter.usePower(board);
        int killed = (int) board.getCharacters().stream().filter(Character::isMurdered).count();
        assertEquals(1, killed);
    }

    @Test
    void stealSomeone() {
        player.setRole(Optional.of(thiefCharacter));
        when(board.getPlayers()).thenReturn(List.of(player));
        thiefCharacter.usePower(board);
        int stole = (int) board.getCharacters().stream().filter(e -> e.getThief().equals(Optional.of(player))).count();
        assertEquals(1, stole);
    }

    @Test
    void swapHandsDeck() {
        player.setRole(Optional.ofNullable(magicianCharacter));

        Deck deck = mock(Deck.class);
        when(board.getPile()).thenReturn(deck);
        when(deck.drawACard()).thenReturn(Optional.of(new Building(BuildingEnum.Caserne)),
                Optional.of(new Building(BuildingEnum.Laboratoire)),
                Optional.of(new Building(BuildingEnum.Cathedrale)),
                Optional.of(new Building(BuildingEnum.Palais)));

        magicianCharacter.swapHandDeck(player);
        List<Building> cards = List.of(
                new Building(BuildingEnum.Caserne),
                new Building(BuildingEnum.Laboratoire),
                new Building(BuildingEnum.Cathedrale),
                new Building(BuildingEnum.Palais)
        );
        assertEquals(cards, player.getCardHand());
    }

    @Test
    void buildArchitect() {
        player.setRole(Optional.of(archiCharacter));
        when(board.getPlayers()).thenReturn(List.of(player));
        archiCharacter.usePower(board);
        assertEquals(3, player.getNbBuildable());
    }

    @Test
    void findPlayer() {
        player.setRole(Optional.of(archiCharacter));
        when(board.getPlayers()).thenReturn(List.of(player));
        when(board.getCharacters()).thenReturn(List.of(archiCharacter));
        archiCharacter.usePower(board);
        assertEquals(3, player.getNbBuildable());
    }

    @Test
    void findPlayerEmpty() {
        player.setRole(Optional.of(archiCharacter));
        when(board.getPlayers()).thenReturn(new ArrayList<>());
        assertEquals(Optional.empty(), archiCharacter.findPlayer(board));
    }

    @Test
    void findPlayerMatchingRole() {
        player.setRole(Optional.of(archiCharacter));
        Player a = new Player(board);
        a.setRole(Optional.of(new Assassin()));
        Player m = new Player(board);
        m.setRole(Optional.of(new Merchant()));
        Player t = new Player(board);
        t.setRole(Optional.of(new Thief()));
        when(board.getPlayers()).thenReturn(List.of(a, m, t));
        assertEquals(Optional.empty(), archiCharacter.findPlayer(board));
    }
}
