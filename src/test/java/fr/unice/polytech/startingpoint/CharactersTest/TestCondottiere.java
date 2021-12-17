package fr.unice.polytech.startingpoint.CharactersTest;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestCondottiere {
    Board board;
    Player player;
    Player archi;
    Player bishop;
    Player condo;
    Character condottiereCharacter;

    @BeforeEach
    void setUp() {
        board = spy(new Board());
        player = spy(new Player(board));
        archi = spy(new Player(board));
        condo = spy(new Player(board));
        bishop = spy(new Player(board));
        condottiereCharacter = spy(Condottiere.class);
    }

    @Test
    void setTaxes() {
        player.refundGold(player.getGold());
        when(player.getCity()).thenReturn(List.of(new Building(BuildingEnum.TourDeGuet),
                new Building(BuildingEnum.Eglise),
                new Building(BuildingEnum.Port),
                new Building(BuildingEnum.Chateau)));
        condottiereCharacter.collectTaxes(player, District.Military);
        assertEquals(1, player.getGold());
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
}
