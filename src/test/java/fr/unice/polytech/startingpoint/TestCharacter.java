package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.*;

import fr.unice.polytech.startingpoint.characters.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.Random;

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
   Player p1;
   Player p2;
   Player p3;
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
        p1 = new Player(b);
        p2 = new Player(b);
        p3 = spy(new Player(b));
        p3.setRole(0);
        b.getCharacters().get(0).setPlayer(p3);
        when(p3.chooseVictim()).thenReturn(b.getCharacters().get(6));
        p2.chooseRole();
        p1.setRole(6);//Become Achitect
        b.getCharacters().get(6).setPlayer(p1);
        condottiere.isTaken();
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
        bishop.setPlayer(p1);
        assertEquals(p1,bishop.getPlayer());
    }
    @Test
    void setPlayerNull(){
        assassin.setPlayer(p2);
        assassin.resetRole();
        assertNull(assassin.getPlayer());
    }
    @Test
    void build3(){//test that the Architect can build up to 3 buildings
        p1.setTaxes(18);//to allow p1 to build every 3 buildings he wants
        p1.play();
        int count=0;
        for (Building b:p1.getBuildings()) {
            if (b.getBuilt()){count+=1;}
        }
        assertEquals(3,count);
    }
    @Test
    void draw2Cards(){
        int numberBuild = p1.getBuildings().size();
        p1.draw2Cards();
        assertEquals(numberBuild+2,p1.getBuildings().size());
    }
    @Test
    public void murder(){
        p3.getRole().usePower();
        assertTrue(p1.getRole().gotMurdered());

    }
}
