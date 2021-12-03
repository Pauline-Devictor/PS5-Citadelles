package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.buildings.Laboratory;
import fr.unice.polytech.startingpoint.buildings.Manufacture;
import fr.unice.polytech.startingpoint.characters.Architect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestPlayer {
    Player p;
    Player pLow;
    Player pHigh;
    Player pBuildings;
    Building eglise;
    Building caserne;
    Building tourDeGuet;
    Board board;

    @BeforeEach
    void setUp(){
        board = new Board();
        pBuildings = spy(new Player(board));
        p = new Player(new Board());
        pLow = spy(new Player(new Board()," ",Strategies.lowGold));
        pHigh = spy(new Player(new Board(),"",Strategies.highGold));
        when(pLow.getCardHand()).thenReturn(new ArrayList<>());
        when(pHigh.getCardHand()).thenReturn(new ArrayList<>());
        eglise = new Building(BuildingEnum.Eglise);
        caserne = new Building(BuildingEnum.Caserne);
        tourDeGuet = new Building(BuildingEnum.TourDeGuet);
    }

    @Test
    void buildGold(){
        p.build(eglise);
        assertEquals(2,p.getGoldScore());
    }

    @Test
    void buildScore(){
        p.build(eglise);
        assertEquals(2,p.getGoldScore());
    }

    @Test
    void hasRole(){
        p.chooseRole();
        assertNotNull(p.getRole());
    }

    @Test
    void isBuildableTestHigher(){
        assertFalse(p.isBuildable(caserne));
    }

    @Test
    void isBuildableTesEqual(){
        assertTrue(p.isBuildable(eglise));
    }

    @Test
    void isBuildableTestLower(){
        assertTrue(p.isBuildable(tourDeGuet));
    }

    @Test
    void preExistingBuilding(){
        p.build(tourDeGuet);
        assertFalse(p.isBuildable(tourDeGuet));
    }
    @Test
    void chooseBuildingLow(){
        System.out.println(pLow);
        assertEquals(eglise,pLow.chooseBuilding(eglise,caserne));

    }
    @Test
    void chooseBuildingHigh(){
        System.out.println(pHigh);
        assertEquals(caserne,pHigh.chooseBuilding(eglise,caserne));
    }

    @Test
    void chooseBuildingNull1(){
        assertEquals(eglise,p.chooseBuilding(eglise,null));
    }
    @Test
    void chooseBuildingNull2(){
        assertEquals(eglise,p.chooseBuilding(null,eglise));
    }
    @Test
    void chooseBuildingNull(){
        assertNull(p.chooseBuilding(null,null));
    }

    @Test
    void drawCards(){
        int size = p.getCardHand().size();
        p.drawCards(3);
        assertEquals(3,p.getCardHand().size()-size);
    }

    @Test
    void drawCardsNeg(){
        int size = p.getCardHand().size();
        p.drawCards(-1);
        assertEquals(0,p.getCardHand().size()-size);
    }

    @Test
    void drawCardsZero(){
        int size = p.getCardHand().size();
        p.drawCards(0);
        assertEquals(0,p.getCardHand().size()-size);
    }

    @Test
    void effectPrestige(){
        ArrayList<Building> b = new ArrayList<>();
        b.add(new Laboratory(BuildingEnum.Laboratoire));
        b.add(new Manufacture(BuildingEnum.Manufacture));
       when(pBuildings.getCity()).thenReturn(b);
       pBuildings.setRole(0);
       //board.getCharacters().get(0).setPlayer(pBuildings);
       pBuildings.play();

    }

}
