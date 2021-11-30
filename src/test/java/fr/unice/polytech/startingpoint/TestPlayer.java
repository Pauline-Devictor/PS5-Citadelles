package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        pLow = new Player(new Board()," ",Strategies.lowGold);
        pHigh = new Player(new Board(),"",Strategies.highGold);
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
    void buildBuilt(){
        p.build(eglise);
        assertTrue(eglise.getBuilt());
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
    void notAlreadyBuilt(){
        assertFalse(p.alreadyBuilt(tourDeGuet));
    }

    @Test
    void preExistingBuilding(){
        p.build(tourDeGuet);
        assertFalse(p.isBuildable(tourDeGuet));
    }
    @Test
    void chooseBuildingLow(){
        assertEquals(eglise,p.chooseBuilding(eglise,caserne));

    }
    @Test
    void chooseBuildingHigh(){
        assertEquals(caserne,pHigh.chooseBuilding(eglise,caserne));
    }
    @Test
    void chooseBuildingNull1(){
        assertEquals(eglise,p.chooseBuilding(eglise,null));
    }
    @Test
    void chooseBuildingNull(){
        assertNull(p.chooseBuilding(null,null));
    }


}
