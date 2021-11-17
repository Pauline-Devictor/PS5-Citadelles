package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.Characters.Assassin;
import fr.unice.polytech.startingpoint.Characters.King;
import fr.unice.polytech.startingpoint.Characters.Condottiere;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCharacter {

   Assassin assassin = new Assassin();
   King king = new King();
   Condottiere condottiere = new Condottiere();

    @BeforeEach
    void setUp(){

    }
    @Test
    void assassinGetOrder(){
        assertEquals(1,assassin.getOrder());
    }
    @Test
    void kingGetOrder(){
        assertEquals(4,king.getOrder());
    }
    @Test
    void condottiereGetOrder(){
        assertEquals(8,condottiere.getOrder());
    }
}
