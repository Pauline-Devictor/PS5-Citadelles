package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.*;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestBuilding {
    Building eglise;
    Player p;

    @BeforeEach
    void setUp() {
        p = spy(new Player(new Board()));
        eglise = new Building(BuildingEnum.Eglise);
    }

    @Test
    void testToString() {
        assertEquals("Eglise, Cout : 2, Quartier : Religion", eglise.toString());
    }

    @Test
    void testLaboratory() {
        when(p.getGold()).thenReturn(25);
        when(p.getCity()).thenReturn(List.of(new Laboratory()));
        int cardHand = p.getCardHand().size();
        p.cityEffects();
        assertEquals(cardHand - 1, p.getCardHand().size());
    }

    @Test
    void testLaboratoryEmptyHand() {
        when(p.getGold()).thenReturn(25);
        when(p.getCity()).thenReturn(List.of(new Laboratory()));
        while (p.getCardHand().size() > 0)
            p.discardCard();
        p.cityEffects();
        assertEquals(0, p.getCardHand().size());
    }

    @Test
    void testManufacture() {
        when(p.getGold()).thenReturn(25);
        when(p.getCity()).thenReturn(List.of(new Manufactory()));
        int cardHand = p.getCardHand().size();
        p.cityEffects();
        assertEquals(3 + cardHand, p.getCardHand().size());
    }

    @Test
    void testManufactureNoGold() {
        Player p = spy(new Player(new Board()));
        when(p.getGold()).thenReturn(0);
        when(p.getCity()).thenReturn(List.of(new Manufactory()));
        int cardHand = p.getCardHand().size();
        p.cityEffects();
        assertEquals(cardHand, p.getCardHand().size());
    }

    /*@Test
    void testLibrary() {
        when(p.getCity()).thenReturn(List.of(new Library()));
        int taille = p.getCardHand().size();
        for (int i = 0; i < taille; i++)
            p.discardCard();
        List<Building> res = p.drawDecision();
        assertEquals(2, res.size());
    }*/

    @Test
    void testLibraryReturn() {
        when(p.getCity()).thenReturn(List.of(new Library()));
        int taille = p.getCardHand().size();
        for (int i = 0; i < taille; i++)
            p.discardCard();
        p.drawDecision();
        assertEquals(2, p.getCardHand().size());
    }

    @Test
    void testObservatory() {
        when(p.getCity()).thenReturn(List.of(new Observatory()));
        int taille = p.getCardHand().size();
        for (int i = 0; i < taille; i++)
            p.discardCard();
        p.drawDecision();
        assertEquals(1, p.getCardHand().size());
    }

}
