package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestLibrary {
    Building eglise;
    Player p;

    @BeforeEach
    void setUp() {
        p = spy(new Player(new Board()));
        eglise = new Building(BuildingEnum.Eglise);
    }

    @Test
    void testLibraryReturn() {
        when(p.getCity()).thenReturn(List.of(new Library()));
        int taille = p.getCardHand().size();
        for (int i = 0; i < taille; i++)
            p.discardCard();
        p.drawDecision();
        assertEquals(2, p.getCardHand().size());
    }
}
