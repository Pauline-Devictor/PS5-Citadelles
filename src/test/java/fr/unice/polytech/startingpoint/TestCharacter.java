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
import static org.mockito.Mockito.*;

public class TestCharacter {
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
    void setTaxes() {
        player.refundGold(player.getGold());
        when(player.getCity()).thenReturn(List.of(new Building(BuildingEnum.Manoir),
                new Building(BuildingEnum.TourDeGuet),
                new Building(BuildingEnum.Port),
                new Building(BuildingEnum.Chateau)));
        king.collectTaxes(player, District.Noble);
        assertEquals(2, player.getGold());
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
    void assassinChooseVictimColor() {
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
        assertTrue(board.getCharactersInfos(4).isMurdered());
    }

    @Test
    void assassinChooseVictimMagician() {
        player.drawAndChoose(4,4);
        when(player.getRole()).thenReturn(assassinCharacter);
        when(board.getPlayers()).thenReturn(List.of(player));
        assassinCharacter.usePower(board);
        assertTrue(board.getCharactersInfos(2).isMurdered());
    }


    @Test
    void stealSomeone() {
        when(player.getRole()).thenReturn(thiefCharacter);
        when(board.getPlayers()).thenReturn(List.of(player));
        thiefCharacter.usePower(board);
        int stole = (int) board.getCharacters().stream().filter(Character::isStolen).count();
        assertEquals(1, stole);
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
                new Building(BuildingEnum.Caserne),
                new Building(BuildingEnum.Laboratoire),
                new Building(BuildingEnum.Cathedrale),
                new Building(BuildingEnum.Palais)
        );
        assertEquals(cards, player.getCardHand());
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


    @Test
    void destroyBuild() {
        archi.takeMoney(25);
        archi.pickRole(6);
        player.pickRole(7);
        archi.build(new Building(BuildingEnum.Manufacture));
        when(board.getPlayers()).thenReturn(List.of(player, archi));
        player.takeMoney(5); // Take money to be able to destroy a build with his power
        condottiereCharacter.usePower(board);
        assertEquals(0, archi.getCity().size());
    }

    @Test
    void destroyAlone() {
        bishop.takeMoney(5);
        player.takeMoney(5); // Take money to be able to destroy a build with his power
        bishop.pickRole(4);
        player.pickRole(7);
        bishop.build(new Building(BuildingEnum.Manufacture));
        player.build(new Building(BuildingEnum.Palais));
        when(board.getPlayers()).thenReturn(List.of(player, bishop));
        condottiereCharacter.usePower(board);
        assertEquals(1, player.getCity().size());
        assertEquals(1, bishop.getCity().size());
    }

    @Test
    void destroyBuildNoGold() {
        archi.takeMoney(25);
        archi.pickRole(6);
        player.pickRole(7);
        archi.build(new Building(BuildingEnum.Manufacture));
        when(board.getPlayers()).thenReturn(List.of(player, archi));
        condottiereCharacter.usePower(board);
        assertEquals(1, archi.getCity().size());
    }

    @Test
    void swapHand() {
        player.drawAndChoose(4,4);
        player.pickRole(3);
        when(player.getRole()).thenReturn(magicianCharacter);
        when(board.getPlayers()).thenReturn(List.of(player));
        magicianCharacter.usePower(board);
        assertTrue(player.getCardHand().size() == 1 );
    }

    @Test
    void magicienChooseTarget() {

    }

    @Test
    void magicienChooseHimself() {

    }

    @Test
    void swapHandPlayer() {

    }


    @Test
    void swapEmptyHandPlayer() {

    }


    @Test
    void merchantGold() { //Merchant get 1 gold when play
        merchant.pickRole(5);
        when(board.getPlayers()).thenReturn(List.of(merchant));
        merchantCharacter.usePower(board);
        assertEquals(3,merchant.getGold());
    }

    @Test
    void merchantEmptyBank() { //Merchant don't get more gold bc empty bank
        merchant.takeMoney(board.getBank().getGold());
        merchant.pickRole(5);
        when(board.getPlayers()).thenReturn(List.of(merchant));
        merchantCharacter.usePower(board);
        assertEquals(12,merchant.getGold());
    }

}
