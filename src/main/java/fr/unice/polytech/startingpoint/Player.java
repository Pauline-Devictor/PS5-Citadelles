package fr.unice.polytech.startingpoint;

import java.util.ArrayList;

public class Player {
    private int gold;
    private final ArrayList<Building> buildings;

    Player(){
        //pour le moment on initialise l'or a 1000 car "illimité"
        gold = 1000;
        buildings = new ArrayList<>();
    }

    void build(Building b){
        b.setBuilt(true);
        gold -= b.getCost();
    }

    void play(Deck d){
        buildings.add(d.drawACard());
        //Construit plus qu'un batiment a la fois
        buildings.stream()
                .filter( b -> b.isBuildable(gold))
                .forEach(this::build);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Or : " + gold + "\nBâtiments :");
        for (Building b : buildings) {
            res.append("\n").append(b.toString()).append(" ");
        }
        return res.toString();
    }

    public int getGold() {
        return gold;
    }
}