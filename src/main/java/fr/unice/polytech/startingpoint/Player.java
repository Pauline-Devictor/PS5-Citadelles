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
    private boolean crown = false;
    private int nbBuildable =1;


    Player(Board b, String name) {
        this.name = name;
        board = b;
        gold = board.getBank().withdrawGold(2);
        buildings = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            buildings.add(board.getPile().drawACard());
        }
        goldScore = 0;
    }

    Player(Board b) {
        this.name = "undefined";
        board = b;
        gold = board.getBank().withdrawGold(2);
        buildings = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            buildings.add(board.getPile().drawACard());
        }
        goldScore = 0;
    }

    boolean build(Building b) {
        boolean buildable = isBuildable(b);
        if (buildable) {
            board.getBank().refundGold(b.getCost());
            gold -= b.getCost();
            goldScore += b.getCost();
            b.build();
        }
        return buildable;
    }

    boolean alreadyBuilt(Building b) {
        for (Building tmp : getBuildings()) {
            if (tmp.getBuilding().equals(b.getBuilding()) && tmp.getBuilt())
                return true;
        }
        return false;
    }

    boolean isBuildable(Building b) {
        return gold >= b.getCost() && !b.getBuilt() && !alreadyBuilt(b);
    }

    void chooseRole() {
        int index;
        do {
            index = new Random().nextInt(8);
        } while (!board.getCharactersInfos(index).isAvailable());
        role = board.getCharactersInfos(index);
        board.getCharactersInfos(index).isTaken();
        System.out.println("Player " + name + " choose " + getRole().getName());
    }

    void play() {
        // chooses to draw a card because there is nothing buildable
        if(getRole().gotMurdered()){
            System.out.println(getName() +" has been killed. His turn is skipped");
        }
        else{
        if (buildings.stream().allMatch(this::alreadyBuilt)) {
            buildings.add(board.getPile().drawACard());
            System.out.println(name + " draw a Card");
        }

        // chooses to get 2 golds because nothing can be built
        else {
            gold += board.getBank().withdrawGold(2);
            System.out.println(name + " take 2 golds");
        }
        getRole().usePower();
        //check if anything can be built and build the first
        for (Building b : buildings) {
            if (build(b)) {
                nbBuildable-=1;
                if (nbBuildable == 0){
                break;
                }
            }
        }}
    }
    void takeCrown(){
        crown=true;
    }
    void leaveCrown(){
        crown=false;
    }
    //Choose victim if assassin
    public Character chooseVictim(){
        Random random = new Random();
        //random.nextInt return integer between 0,6 and we want between 1,7 so rdm.nextInt +1
        int i = random.nextInt(7)+1;
        return board.getCharacters().get(i);
    }
    public void setNbBuildable(int number){
        this.nbBuildable=number;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Or : " + gold);
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

    public Character getRole() {
        return role;
    }
    
    public ArrayList<Building> getBuildings() {
        return (ArrayList<Building>) buildings.clone();

    }
    public boolean getCrown(){
        return crown;
    }
}