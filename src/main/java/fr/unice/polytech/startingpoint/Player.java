package fr.unice.polytech.startingpoint;

import java.util.ArrayList;

public class Player {
    private final String name;
    private int gold;
    private int goldScore;
    private final ArrayList<Building> buildings;


    Player(){
        //pour le moment on initialise l'or a 1000 car "illimité"
        this.name="Non defini";
        gold = 1000;
        buildings = new ArrayList<>();
        goldScore=0;
    }
    Player(String name){
        //pour le moment on initialise l'or a 1000 car "illimité"
        this.name=name;
        gold = 1000;
        buildings = new ArrayList<>();
        goldScore=0;
    }

    void build(Building b){
        if (b.isBuildable(getGold())) {
            gold -= b.getCost();
            goldScore += b.getCost();
            b.build();
        }
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
        StringBuilder res = new StringBuilder("Or : " + gold );
        res.append("\nScore des Bâtiments : ").append(goldScore).append("\nBâtiments :");
        for (Building b : buildings) {
            res.append("\n").append(b.toString()).append(" ");
        }
        return res.toString();
    }

    public int getGold() {
        return gold;
    }

    public int getGoldScore() {
        return goldScore;
    }

    public String getName() {
        return name;
    }
}