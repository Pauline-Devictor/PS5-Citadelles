package fr.unice.polytech.startingpoint;

import java.util.ArrayList;

public class Joueur {
    private int or;
    private ArrayList<Batiment> batiments;
    private Deck deck;

    Joueur(Deck d){
        //pour le moment on initialise l'or a 1000 car "illimité"
        or = 1000;
        batiments = new ArrayList<Batiment>();
        deck =d;
    }

    void construit(Batiment b){
        b.setConstruit(true);
        or -= b.getCout();
    }

    void joue(){
        batiments.add(deck.pioche());
        batiments.stream()
                .filter( b -> b.isConstructible(or))
                .forEach(b -> construit(b));
    }

    @Override
    public String toString() {
        String res = "Or : " + or + "\nBatiments :";
        for (Batiment b : batiments) {
            res += "\n" + b.toString() + " ";
        }
        return res;
    }
}