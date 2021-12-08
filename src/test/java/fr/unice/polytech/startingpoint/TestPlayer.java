package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.*;
import fr.unice.polytech.startingpoint.strategies.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        pLow = spy(new RushMerch(new Board(), " "));
        pHigh = spy(new HighScoreThief(new Board(), ""));
        when(pLow.getCardHand()).thenReturn(new ArrayList<>());
        when(pHigh.getCardHand()).thenReturn(new ArrayList<>());
        eglise = new Building(BuildingEnum.Eglise);
        caserne = new Building(BuildingEnum.Caserne);
        tourDeGuet = new Building(BuildingEnum.TourDeGuet);
    }

    @Test
    void buildGold() {
        p.build(eglise);
        assertEquals(2, p.getGoldScore());
    }

    @Test
    void buildScore() {
        p.build(eglise);
        assertEquals(2, p.getGoldScore());
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
    void chooseBuildingLow() {
        assertEquals(eglise, pLow.chooseBuilding(eglise, caserne));

    }

    @Test
    void chooseBuildingHigh() {
        assertEquals(caserne, pHigh.chooseBuilding(eglise, caserne));
    }

    @Test
    void chooseBuildingNull1() {
        assertEquals(eglise, p.chooseBuilding(eglise, null));
    }

    @Test
    void chooseBuildingNull2() {
        assertEquals(eglise, p.chooseBuilding(null, eglise));
    }

    @Test
    void chooseBuildingNull() {
        assertNull(p.chooseBuilding(null, null));
    }

    @Test
    void drawCards() {
        int size = p.getCardHand().size();
        p.drawCards(3);
        assertEquals(3, p.getCardHand().size() - size);
    }

    @Test
    void drawCardsNeg() {
        int size = p.getCardHand().size();
        p.drawCards(-1);
        assertEquals(0, p.getCardHand().size() - size);
    }

    @Test
    void drawCardsZero() {
        int size = p.getCardHand().size();
        p.drawCards(0);
        assertEquals(0, p.getCardHand().size() - size);
    }

    @Test
    void collectTaxesEmptyBank() {
        Bank bankEmpty = spy(new Bank(30));
        when(bankEmpty.getGold()).thenReturn(0);
        when(board.getBank()).thenReturn(bankEmpty);
        Player p = new Player(board);
        assertEquals(0, p.collectTaxes());
    }

    @Test
    void chooseBuildingsList() {
        Optional<Building> b = p.chooseBuilding(List.of(new Library(), new Laboratory(), new Building(BuildingEnum.Temple)));
        assertEquals(new Library(), b.orElse(null));
    }

    @Test
    void chooseBuildingsListLow() {
        Optional<Building> b = pLow.chooseBuilding(List.of(new Library(), new Laboratory(), new Building(BuildingEnum.Temple)));
        assertEquals(new Building(BuildingEnum.Temple), b.orElse(null));
    }
}
