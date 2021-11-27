package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCharacter {
   Assassin assassin = new Assassin();
   Thief thief = new Thief();
   Magician magician = new Magician();
   King king = new King();
   Bishop bishop = new Bishop();
   Merchant merchant = new Merchant();
   Architect architect = new Architect();
   Condottiere condottiere = new Condottiere();
   Player p1;
   Player p2;
   Board b;

    @BeforeEach
    void setUp(){
        b = new Board();
        p1 = new Player(b,"Bob");
        p2 = new Player(b,"Billy");
        p2.chooseRole();
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
}
