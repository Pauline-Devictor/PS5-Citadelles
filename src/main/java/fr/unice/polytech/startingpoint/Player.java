package fr.unice.polytech.startingpoint;

import java.util.ArrayList;

public class Player {
    private final String name;
    private int gold;
    private int goldScore;
    private final ArrayList<Building> buildings;
    private final Deck deck;
    private Character role;


    Player(Deck d, String name){
        this.name = name;
        //every player starts with 2 golds
        deck = d;
        gold = 2;
        buildings = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            buildings.add(d.drawACard());
        }
        goldScore=0;
    }
    Player(Deck d){
        this.name = "undefined";
        //every player starts with 2 golds
        deck = d;
        gold = 2;
        buildings = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            buildings.add(d.drawACard());
        }
        goldScore=0;
    }

    boolean build(Building b){
        boolean buildable = b.isBuildable(getGold());
        if (buildable) {
            gold -= b.getCost();
            goldScore += b.getCost();
            b.build();
            }
        return buildable;
    }

    void play(){
        // choses to draw a card because hand is empty
        if(buildings.stream().allMatch(Building::getBuilt) || buildings.isEmpty()){
            buildings.add(deck.drawACard());
        }
        // choses to get 2 golds because nothing can be built
        else{
            gold += 2;
        }
        //check if anything can be built and build the first
        for (Building b : buildings) {
             if(build(b)){
                 break;
             }
        }
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