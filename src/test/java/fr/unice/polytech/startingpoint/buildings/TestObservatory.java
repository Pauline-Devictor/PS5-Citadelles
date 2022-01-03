package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestObservatory {
    Building eglise;
    Player p;

    @BeforeEach
    void setUp() {
        p = spy(new Player(new Board()));
        eglise = new Building(BuildingEnum.Eglise);
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
