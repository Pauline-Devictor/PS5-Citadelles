package fr.unice.polytech.startingpoint.strategiesTest;

import fr.unice.polytech.startingpoint.Bank;
import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.Deck;
import fr.unice.polytech.startingpoint.buildings.*;
import fr.unice.polytech.startingpoint.strategies.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.unice.polytech.startingpoint.strategies.Player.PointsOrder;
import static fr.unice.polytech.startingpoint.strategies.Player.RoleOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestPlayer {
    Player p;
    Player pLow;
    Player pHigh;
    Player pSpy;
    Building eglise;
    Building caserne;
    Building tourDeGuet;
    Board board;

    @BeforeEach
    void setUp() {
        board = spy(new Board());
        pSpy = spy(new Player(board));
        p = new Player(new Board());
        pLow = spy(new RushMerch(board));
        pHigh = spy(new HighScoreThief(new Board()));
        eglise = new Building(BuildingEnum.Eglise);
        caserne = new Building(BuildingEnum.Caserne);
        tourDeGuet = new Building(BuildingEnum.TourDeGuet);
    }

    @Test
    void buildGold() {
        p.build(eglise);
        assertEquals(2, p.getScore());
    }

    @Test
    void buildScore() {
        p.build(eglise);
        assertEquals(2, p.getScore());
    }

    @Test
    void hasRole() {
        p.chooseRole();
        assertNotNull(p.getRole());
    }

    @Test
    void isBuildableTestHigher() {
        assertFalse(p.isBuildable(caserne));
    }

    @Test
    void isBuildableTesEqual() {
        assertTrue(p.isBuildable(eglise));
    }

    @Test
    void isBuildableTestLower() {
        assertTrue(p.isBuildable(tourDeGuet));
    }

    @Test
    void preExistingBuilding() {
        p.build(tourDeGuet);
        assertFalse(p.isBuildable(tourDeGuet));
    }

    @Test
    void drawCards() {
        int size = p.getCardHand().size();
        p.drawAndChoose(3, 3);
        assertEquals(3, p.getCardHand().size() - size);
    }

    @Test
    void drawCardsNeg() {
        int size = p.getCardHand().size();
        p.drawAndChoose(2, -1);
        assertEquals(0, p.getCardHand().size() - size);
    }

    @Test
    void drawCardsZero() {
        int size = p.getCardHand().size();
        p.drawAndChoose(2, 0);
        assertEquals(0, p.getCardHand().size() - size);
    }

    @Test
    void drawCardsLessThanSelect() {
        int size = p.getCardHand().size();
        p.drawAndChoose(2, 3);
        assertEquals(2, p.getCardHand().size() - size);
    }

    @Test
    void sortPoints() {
        when(pSpy.getScore()).thenReturn(10);
        when(pHigh.getScore()).thenReturn(7);
        when(pLow.getScore()).thenReturn(5);
        List<Player> players = new ArrayList<>(List.of(pSpy, pLow, pHigh));
        players.sort(PointsOrder);
        assertEquals(List.of(pSpy, pHigh, pLow), players);
    }

    @Test
    void sortPointsEven() {
        when(pSpy.getScore()).thenReturn(10);
        when(pHigh.getScore()).thenReturn(7);
        when(pLow.getScore()).thenReturn(7);
        List<Player> players = new ArrayList<>(List.of(pHigh, pSpy, pLow));
        players.sort(PointsOrder);
        assertEquals(List.of(pSpy, pHigh, pLow), players);
    }

    @Test
    void sortRoleOrder() {
        p.pickRole(0);
        pLow.pickRole(3);
        pHigh.pickRole(2);
        pSpy.pickRole(1);
        List<Player> players = new ArrayList<>(List.of(pHigh, pSpy, pLow, p));
        players.sort(RoleOrder);
        assertEquals(List.of(p, pSpy, pHigh, pLow), players);
    }

    @Test
    void drawDecisionEmptyBank() {
        when(board.getBank()).thenReturn(new Bank(0));
        assertTrue(pSpy.drawOrGold());
    }

    @Test
    void drawDecisionEmptyDeck() {
        when(board.getBank()).thenReturn(new Bank(0));
        assertTrue(pSpy.drawOrGold());
    }

    @Test
    void drawDecisionNothingBuildable() {
        when(pSpy.getCardHand()).thenReturn(new ArrayList<>());
        assertTrue(pSpy.drawOrGold());
    }

    @Test
    void chooseBuildingMore() {
        int size = pSpy.getCardHand().size();
        for (int i = 0; i < size; i++)
            pSpy.discardCard();
        List<Building> builds = new ArrayList<>(List.of(new Library(),
                new Building(BuildingEnum.Manufacture),
                new Building(BuildingEnum.Manoir)));
        assertEquals(builds, pSpy.chooseBuilding(builds, 12));
    }

    @Test
    void chooseBuildingEmpty() {
        int size = pSpy.getCardHand().size();
        for (int i = 0; i < size; i++)
            pSpy.discardCard();
        when(pSpy.getCardHand()).thenReturn(List.of(
                new Building(BuildingEnum.Manufacture),
                new Library()));
        List<Building> builds = new ArrayList<>(List.of(new Library(),
                new Building(BuildingEnum.Manufacture),
                new Building(BuildingEnum.Manoir)));
        assertEquals(List.of(new Building(BuildingEnum.Manoir)), pSpy.chooseBuilding(builds, 1));
    }

    @Test
    void chooseBuildingNeg() {
        int size = pSpy.getCardHand().size();
        for (int i = 0; i < size; i++)
            pSpy.discardCard();
        List<Building> builds = new ArrayList<>(List.of(new Library(),
                new Building(BuildingEnum.Manufacture),
                new Building(BuildingEnum.Manoir)));
        assertEquals(new ArrayList<>(), pSpy.chooseBuilding(builds, -1));
    }

    @Test
    void chooseBuilding() {
        int size = pSpy.getCardHand().size();
        for (int i = 0; i < size; i++)
            pSpy.discardCard();
        List<Building> builds = new ArrayList<>(List.of(new Library(),
                new Building(BuildingEnum.Manufacture),
                new Building(BuildingEnum.Manoir)));
        assertEquals(List.of(new Building(BuildingEnum.Manoir), new Building(BuildingEnum.Manufacture)), pSpy.chooseBuilding(builds, 2));
    }

    @Test
    void roleAvailable() {
        pSpy.pickRole(3);
        assertFalse(board.getCharactersInfos(3).isAvailable());
    }

    @Test
    void roleAvailableSet() {
        pSpy.pickRole(3);
        assertEquals(board.getCharactersInfos(3), pSpy.getRole());
    }


    @Test
    void roleWrongIndexNeg() {
        pSpy.pickRole(-1);
        assertEquals(board.getCharactersInfos(0), pSpy.getRole());
    }

    @Test
    void roleWrongIndex() {
        pSpy.pickRole(25);
        assertEquals(board.getCharactersInfos(7), pSpy.getRole());
    }

    @Test
    void refundGold() {
        pSpy.refundGold(pSpy.getGold());
        assertEquals(0, pSpy.getGold());
    }

    @Test
    void refundGoldNeg() {
        pSpy.refundGold(pSpy.getGold());
        pSpy.refundGold(-2);
        assertEquals(0, pSpy.getGold());
    }

    @Test
    void refundGoldUnavailable() {
        pSpy.refundGold(25);
        assertEquals(0, pSpy.getGold());
    }

    @Test
    void takeMoneyEmptyBank() {
        when(board.getBank()).thenReturn(new Bank(0));
        pSpy.takeMoney(3);
        assertEquals(2, pSpy.getGold());
    }

    @Test
    void takeMoney() {
        pSpy.takeMoney(3);
        assertEquals(5, pSpy.getGold());
    }

    @Test
    void bonusStandard() {
        // District | Prestiges | City
        when(pSpy.getCity()).thenReturn(List.of(
                new Library(),
                new Observatory(),
                new Building(BuildingEnum.Manoir),
                new Building(BuildingEnum.Echoppe),
                new Building(BuildingEnum.Temple),
                new Building(BuildingEnum.Palais),
                new MiracleCourtyard(),
                new Building(BuildingEnum.Prison)
        ));
        assertTrue(pSpy.calculBonus()[2]);
        assertTrue(pSpy.calculBonus()[0]);
        assertTrue(pSpy.calculBonus()[1]);
    }

    @Test
    void bonusDistricts() {
        when(pSpy.getCity()).thenReturn(List.of(
                new Building(BuildingEnum.Manoir),
                new Building(BuildingEnum.Temple),
                new Building(BuildingEnum.Palais),
                new MiracleCourtyard(),
                new Building(BuildingEnum.Prison),
                new Building(BuildingEnum.Echoppe)
        ));
        assertTrue(pSpy.calculBonus()[0]);
        assertFalse(pSpy.calculBonus()[1]);
        assertFalse(pSpy.calculBonus()[2]);
    }


    @Test
    void bonusPrestiges() {
        when(pSpy.getCity()).thenReturn(List.of(
                new Building(BuildingEnum.Manoir),
                new Building(BuildingEnum.Temple),
                new Building(BuildingEnum.Palais),
                new MiracleCourtyard(),
                new Building(BuildingEnum.Prison),
                new Library()
        ));
        assertFalse(pSpy.calculBonus()[0]);
        assertTrue(pSpy.calculBonus()[1]);
        assertFalse(pSpy.calculBonus()[2]);
    }

    @Test
    void bonusPrestigeFalse() {
        when(pSpy.getCity()).thenReturn(List.of(
                new Building(BuildingEnum.Manoir),
                new Building(BuildingEnum.Temple),
                new Building(BuildingEnum.Palais),
                new MiracleCourtyard(),
                new Building(BuildingEnum.Prison)
        ));
        assertFalse(pSpy.calculBonus()[0]);
        assertFalse(pSpy.calculBonus()[1]);
        assertFalse(pSpy.calculBonus()[2]);
    }

    @Test
    void discardCard() {
        Deck deck = mock(Deck.class);
        when(deck.drawACard()).thenReturn(Optional.of(new Library()),
                Optional.of(new Building(BuildingEnum.Manoir)),
                Optional.of(new Building(BuildingEnum.Palais)),
                Optional.of(new Building(BuildingEnum.Temple)));
        when(pLow.getCity()).thenReturn(new ArrayList<>());
        when(board.getPile()).thenReturn(deck);
        while (pLow.getCardHand().size() != 0)
            pLow.discardCard();
        pLow.drawAndChoose(4, 4);
        pLow.discardCard();
        assertEquals(List.of(new Building(BuildingEnum.Palais), new Building(BuildingEnum.Manoir), new Building(BuildingEnum.Temple))
                , pLow.getCardHand());
    }
}
