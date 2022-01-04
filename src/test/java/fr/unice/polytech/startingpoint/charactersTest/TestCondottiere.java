package fr.unice.polytech.startingpoint.charactersTest;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.buildings.Manufactory;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestCondottiere {
    Board board;
    Player archi;
    Player bishop;
    Player condo;
    Character condottiereCharacter;

    @BeforeEach
    void setUp() {
        LOGGER.setLevel(Level.OFF);
        board = spy(new Board());
        archi = spy(new Player(board));
        condo = spy(new Player(board));
        bishop = spy(new Player(board));
        condottiereCharacter = spy(Condottiere.class);
    }

    @Test
    void setTaxes() {
        condo.refundGold(condo.getGold());
        when(condo.getCity()).thenReturn(List.of(new Building(BuildingEnum.TourDeGuet),
                new Building(BuildingEnum.Eglise),
                new Building(BuildingEnum.Port),
                new Building(BuildingEnum.Chateau)));
        condottiereCharacter.collectTaxes(condo, District.Military);
        assertEquals(1, condo.getGold());
    }
    @Test
    void destroyNoBuild() {
        archi.takeMoney(25);
        archi.pickRole(6);
        condo.pickRole(7);
        archi.build(new Building(BuildingEnum.Manufacture));
        when(board.getPlayers()).thenReturn(List.of(condo, archi));
        condo.takeMoney(5); // Take money to be able to destroy a build with his power
        condottiereCharacter.usePower(board);
        //too expensive to destroy (archi has only one card, so not worth it)
        assertEquals(1, archi.getCity().size());
    }

    @Test
    void destroyMostExpensiveBuild() {
        archi.takeMoney(25);
        archi.pickRole(6);
        condo.pickRole(7);
        archi.build(new Manufactory());
        archi.build(new Building(BuildingEnum.Eglise));
        archi.build(new Building(BuildingEnum.Echoppe));
        archi.build(new Building(BuildingEnum.Port));
        archi.build(new Building(BuildingEnum.Temple));
        archi.build(new Building(BuildingEnum.TourDeGuet));

        when(board.getPlayers()).thenReturn(List.of(condo, archi));
        condo.takeMoney(12); // Take money to be able to destroy a build with his power
        condottiereCharacter.usePower(board);
        assertFalse(archi.getCity().contains(new Manufactory()));
    }

    @Test
    void destroyLeastExpensiveBuild() {
        archi.takeMoney(25);
        archi.pickRole(6);
        condo.pickRole(7);
        archi.build(new Manufactory());
        archi.build(new Building(BuildingEnum.Eglise));
        archi.build(new Building(BuildingEnum.Echoppe));
        archi.build(new Building(BuildingEnum.Port));
        archi.build(new Building(BuildingEnum.Temple));
        archi.build(new Building(BuildingEnum.Marche));

        when(board.getPlayers()).thenReturn(List.of(condo, archi));
        condo.takeMoney(5); // Take money to be able to destroy a build with his power
        condottiereCharacter.usePower(board);
        assertFalse(archi.getCity().contains(new Building(BuildingEnum.Temple)));
    }

    @Test
    void destroyNoBishop() {
        bishop.takeMoney(5);
        condo.takeMoney(5); // Take money to be able to destroy a build with his power
        bishop.pickRole(4);
        condo.pickRole(7);
        bishop.build(new Building(BuildingEnum.Manufacture));
        condo.build(new Building(BuildingEnum.Chateau));
        when(board.getPlayers()).thenReturn(List.of(condo, bishop));
        condottiereCharacter.usePower(board);
        assertEquals(1, bishop.getCity().size());
        assertEquals(1, condo.getCity().size());
    }

    @Test
    void destroyBuildNoGold() {
        archi.takeMoney(25);
        archi.pickRole(6);
        condo.pickRole(7);
        archi.build(new Building(BuildingEnum.Manufacture));
        when(board.getPlayers()).thenReturn(List.of(condo, archi));
        condottiereCharacter.usePower(board);
        assertEquals(1, archi.getCity().size());
    }

    @Test
    void destroyFreeBuild() {
        archi.takeMoney(25);
        archi.pickRole(6);
        condo.pickRole(7);
        archi.build(new Manufactory());
        archi.build(new Building(BuildingEnum.TourDeGuet));
        when(board.getPlayers()).thenReturn(List.of(condo, archi));
        condo.takeMoney(12); // Take money to be able to destroy a build with his power
        condottiereCharacter.usePower(board);
        assertFalse(archi.getCity().contains(new Building(BuildingEnum.TourDeGuet)));
    }
}
