package fr.unice.polytech.startingpoint;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private ArrayList<Batiment> batiments = new ArrayList<Batiment>();

    Deck(){
        //pour l'instant on crée une pioche de carte basique
        for(int i = 2; i < 15; i++){
            batiments.add(new Batiment(i));
        }
    }

     Batiment pioche(){
        if (batiments.isEmpty()){
            //pour l'instant
            return null;
        }
        Batiment b = batiments.get(new Random().nextInt(batiments.size()-1));
        batiments.remove(b);
        return b;
    }
}
