package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;

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
        architectOne.setRole(6);
        when(architectOne.getGold()).thenReturn(26);
        b.getCharacters().get(6).setPlayer(architectOne);
        p2 = new Player(b);

        assassinOne = spy(new Player(b));
        assassinOne.setRole(0);
        b.getCharacters().get(0).setPlayer(assassinOne);
        when(assassinOne.chooseVictim()).thenReturn(b.getCharacters().get(6));

        condottiere.isTaken();

        kingOne= spy(new Player(b));
        kingOne.setRole(3);//Become King
        b.getCharacters().get(3).setPlayer(kingOne);
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

        when(kingOne.getCardHand()).thenReturn(buildings);

        bishopOne = spy(new Player(b));
        bishopOne.setRole(4);//Become King
        b.getCharacters().get(4).setPlayer(bishopOne);
        when(bishopOne.getCardHand()).thenReturn(buildings);

        merchantOne = spy(new Player(b));
        merchantOne.setRole(5);//Become King
        b.getCharacters().get(5).setPlayer(merchantOne);
        when(merchantOne.getCardHand()).thenReturn(buildings);

        condottiereOne = spy(new Player(b));
        condottiereOne.setRole(7);//Become King
        b.getCharacters().get(7).setPlayer(condottiereOne);
        when(condottiereOne.getCardHand()).thenReturn(buildings);
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
        int numberBuild = architectOne.getCardHand().size();
        architectOne.drawCards(2);
        assertEquals(numberBuild+2, architectOne.getCardHand().size());
    }
    @Test
    public void murder(){
        assassinOne.getRole().usePower();
        assertTrue(architectOne.getRole().gotMurdered());
    }
    @Test
    public void taxesKing(){
        kingOne.play();
        assertEquals(kingOne.getTaxes(),1);
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
