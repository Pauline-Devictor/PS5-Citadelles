package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.Test;

public class TestJoueur {
    @Test
    void testToString(){
        Joueur j = new Joueur(new Deck());
        j.joue();
        j.joue();
        System.out.println(j.toString());
    }

    @Test
    void testConstruit(){
        Joueur j = new Joueur(new Deck());
    }
}
