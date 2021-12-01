package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestCharacter {
   Assassin assassin;
   Thief thief;
   Magician magician;
   King king;
   Bishop bishop;
   Merchant merchant;
   Architect architect;
   Condottiere condottiere;
   Player architectOne;
   Player p2;
   Player assassinOne;
   Player merchantOne;
   Player kingOne;
   Player bishopOne;
   Player condottiereOne;
   Board b;

    @BeforeEach
    void setUp(){
        assassin = new Assassin();
        thief = new Thief();
        magician = new Magician();
        king = new King();
        bishop = new Bishop();
        merchant = new Merchant();
        architect = new Architect();
        condottiere = new Condottiere();
        b = new Board();

        architectOne = spy(new Player(b));
        when(architectOne.getRole()).thenReturn(architect);
        when(architectOne.getGold()).thenReturn(26);
        architect.setPlayer(architectOne);
        b.getCharacters().get(6).setPlayer(architectOne);
        p2 = new Player(b);

        assassinOne = spy(new Player(b));
        when(assassinOne.getRole()).thenReturn(assassin);
        assassin.setPlayer(assassinOne);
        b.getCharacters().get(0).setPlayer(assassinOne);
        when(assassinOne.chooseVictim()).thenReturn(b.getCharacters().get(6));

        condottiere.isTaken();

        kingOne= spy(new Player(b));
        when(kingOne.getRole()).thenReturn(king);
        b.getCharacters().get(3).setPlayer(kingOne);
        king.setPlayer(kingOne);

        ArrayList<Building> buildings = new ArrayList<>();
        buildings.add(new Building(BuildingEnum.Manoir));//Noble
        buildings.add(new Building(BuildingEnum.Temple));//Religion
        buildings.add(new Building(BuildingEnum.Eglise));
        buildings.add(new Building(BuildingEnum.Taverne));//Commercial
        buildings.add(new Building(BuildingEnum.Echoppe));
        buildings.add(new Building(BuildingEnum.Marche));
        buildings.add(new Building(BuildingEnum.TourDeGuet));//Military
        buildings.add(new Building(BuildingEnum.Prison));
        buildings.add(new Building(BuildingEnum.Caserne));
        buildings.add(new Building(BuildingEnum.Forteresse));

        when(kingOne.getBuildings()).thenReturn(buildings);

        bishopOne = spy(new Player(b));
        when(bishopOne.getRole()).thenReturn(bishop);
        b.getCharacters().get(4).setPlayer(bishopOne);
        when(bishopOne.getBuildings()).thenReturn(buildings);
        bishop.setPlayer(bishopOne);

        merchantOne = spy(new Player(b));
        when(merchantOne.getRole()).thenReturn(merchant);
        b.getCharacters().get(5).setPlayer(merchantOne);
        when(merchantOne.getBuildings()).thenReturn(buildings);
        merchant.setPlayer(merchantOne);

        condottiereOne = spy(new Player(b));
        when(condottiereOne.getRole()).thenReturn(condottiere);
        b.getCharacters().get(7).setPlayer(condottiereOne);
        when(condottiereOne.getBuildings()).thenReturn(buildings);
        condottiere.setPlayer(condottiereOne);
    }
    @Test
    void assassinGetOrder(){
        assertEquals(1,assassin.getOrder());
    }
    @Test
    void thiefGetOrder(){
        assertEquals(2,thief.getOrder());
    }
    @Test
    void magicianGetOrder(){
        assertEquals(3,magician.getOrder());
    }
    @Test
    void kingGetOrder(){
        assertEquals(4,king.getOrder());
    }
    @Test
    void bishopGetOrder(){
        assertEquals(5,bishop.getOrder());
    }
    @Test
    void merchantGetOrder(){
        assertEquals(6,merchant.getOrder());
    }
    @Test
    void architectGetOrder(){
        assertEquals(7,architect.getOrder());
    }
    @Test
    void condottiereGetOrder(){
        assertEquals(8,condottiere.getOrder());
    }
    @Test
    void isAvailableTrue(){assertTrue(king.isAvailable());}
    @Test
    void isNotAvailable(){
        king.isTaken();
        assertFalse(king.isAvailable());}
    @Test
    void is_free(){
        condottiere.resetRole();
        assertTrue(condottiere.isAvailable());
    }
    @Test
    void roleName(){
        assertEquals("Assassin",assassin.getName());
    }
    @Test
    void setPlayer(){
        bishop.setPlayer(architectOne);
        assertEquals(architectOne,bishop.getPlayer());
    }
    @Test
    void setPlayerNull(){
        assassin.setPlayer(p2);
        assassin.resetRole();
        assertNull(assassin.getPlayer());
    }
    @Test
    void build3(){//test that the Architect can build up to 3 buildings
        architectOne.play();
        verify(architectOne, times(3)).build(any());
        /*int count=0;
        for (Building b: architectOne.getBuildings()) {
            if (b.getBuilt()){count+=1;}
        }
        assertEquals(3,count);*/
    }

    @Test
    void draw2Cards(){
        int numberBuild = architectOne.getBuildings().size();
        architectOne.draw2Cards();
        assertEquals(numberBuild+2, architectOne.getBuildings().size());
    }
    @Test
    public void murder(){
        assassinOne.getRole().usePower();
        assertTrue(b.getCharactersInfos(6).gotMurdered());
    }
    @Test
    public void taxesKing(){
        kingOne.play();
        assertEquals(b.getCharactersInfos(3).getPlayer().getTaxes(),1);
    }
    @Test
    public void taxesBishop(){
        bishopOne.play();
        assertEquals(bishopOne.getTaxes(),2);
    }
    @Test
    public void taxesMerchant(){
        merchantOne.play();
        assertEquals(merchantOne.getTaxes(),3);
    }
    @Test
    public void taxesCondottiere(){
        condottiereOne.play();
        assertEquals(condottiereOne.getTaxes(),4);
    }
}
