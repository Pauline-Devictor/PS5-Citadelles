package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestPlayer {
    Player p;
    Player pLow;
    Player pHigh;
    Building eglise;
    Building caserne;
    Building tourDeGuet;

    @BeforeEach
    void setUp(){
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
    }/*
    @Test
    void buildBuilt(){
        p.build(eglise);
        assertTrue(eglise.getBuilt());
    }*/

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


}
