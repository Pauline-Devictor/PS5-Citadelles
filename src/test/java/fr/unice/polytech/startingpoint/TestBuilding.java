package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestBuilding {
    Building eglise;

    @BeforeEach
    void setUp() {
        eglise = new Building(BuildingEnum.Eglise);
    }

    @Test
    void testToString() {
        assertEquals("Eglise, Cout : 2, Quartier : Religion", eglise.toString());
    }

    @Test
    void testLaboratory() {

    }

    @Test
    void testObservatory() {

    }
    
    void testManufacture() {
        Player p = spy(new Player(new Board()));
        when(p.getGold()).thenReturn(25);
        when(p.getCity()).thenReturn(List.of(new Building(BuildingEnum.Manufacture)));
        //System.out.println(p.getCity()+"\n\n"+p.getCardHand());
        int cardHand = p.getCardHand().size();
        p.cityEffects();
        assertEquals(3 + cardHand, p.getCardHand().size());
    }

    @Test
    void testBibliotheque() {

    }
}
