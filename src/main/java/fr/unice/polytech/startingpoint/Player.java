package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.Character;
import java.util.ArrayList;
import java.util.Random;

public class Player {
    private final String name;
    private int gold;
    private int goldScore;
    private final ArrayList<Building> buildings;
    private final Board board;
    private Character role;


    Player(Board b, String name){
        this.name = name;
        board = b;
        gold = board.getBank().withdrawGold(2);
        buildings = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            buildings.add(board.getPile().drawACard());
        }
        goldScore=0;
    }

    Player(Board b){
        this.name = "undefined";
        board = b;
        gold = board.getBank().withdrawGold(2);
        buildings = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            buildings.add(board.getPile().drawACard());
        }
        goldScore=0;
    }

    boolean build(Building b){
        boolean buildable = b.isBuildable(getGold());
        if (buildable) {
            board.getBank().refundGold(b.getCost());
            gold -= b.getCost();
            goldScore += b.getCost();
            b.build();
            }
        return buildable;
    }

    void chooseRole(){
        int index;
        do {
            index = new Random().nextInt(8);
        }while (!board.getCharactersInfos(index).isAvailable());
        role = board.getCharactersInfos(index);
        board.getCharactersInfos(index).isTaken();
        System.out.println("Player "+ name +" choose " + getRole().getName());
        }

    void play(){
        // chooses to draw a card because hand is empty
        if(buildings.stream().allMatch(Building::getBuilt) || buildings.isEmpty()){
            buildings.add(board.getPile().drawACard());
            System.out.println(name+" draw a Card");
        }
        // chooses to get 2 golds because nothing can be built
        else{
            gold += board.getBank().withdrawGold(2);
            System.out.println(name+" take 2 golds");
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
        res.append("\nBâtiments :").append("\tScore des Bâtiments : ").append(goldScore);
        for (Building b : buildings) {
            res.append("\n\t").append(b.toString()).append(" ");
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

    public Character getRole(){return role;}

    public ArrayList<Building> getBuildings() {
        return (ArrayList<Building>) buildings.clone();
    }
}