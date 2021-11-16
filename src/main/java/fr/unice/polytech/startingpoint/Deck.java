package fr.unice.polytech.startingpoint;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private final ArrayList<Building> buildings;

    Deck(){
        buildings = new ArrayList<>();
        //pour l'instant on crée une pioche de carte basique
        for(int i = 0; i < 54; i++){
            if(i>46)
                buildings.add(new Building(5));
            else if(i>39)
                buildings.add(new Building(4));
            else if(i>25)
                buildings.add(new Building(3));
            else if(i>11)
                buildings.add(new Building(2));
            else
                buildings.add(new Building(1));
        }
    }

     Building drawACard(){
        if (buildings.isEmpty()){
            //pour l'instant
            return null;
        }
        Building b = buildings.get(new Random().nextInt(buildings.size()-1));
        buildings.remove(b);
        return b;
    }
}
