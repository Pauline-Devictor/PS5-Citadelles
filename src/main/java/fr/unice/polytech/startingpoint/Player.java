package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.Character;

import java.util.ArrayList;
import java.util.Random;

import static fr.unice.polytech.startingpoint.Main.*;
import static java.util.Objects.isNull;

public class Player {
    private final String name;
    private int gold;
    private int goldScore;
    private final ArrayList<Building> buildings;
    private final Board board;
    private Character role;
    private boolean crown = false;


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
    }

    void play() {
        int goldSave = getGold();
        boolean draw = buildings.stream().allMatch(this::alreadyBuilt);
        Building checkBuilding = null;
        Building checkDraw=null;
        // chooses to draw a card because there is nothing buildable
        if (draw){
            checkDraw=board.getPile().drawACard();
            buildings.add(checkDraw);
        }
        // chooses to get 2 golds because nothing can be built
        else
            gold += board.getBank().withdrawGold(2);
        //check if anything can be built and build the first
        for (Building b : buildings) {
            if (build(b)) {
                checkBuilding = b;
                break;
            }
        }
        showPlay(goldSave, checkDraw, checkBuilding);
    }

    private void showPlay(int goldSave, Building checkDraw, Building checkBuilding) {
        int showGold = (getGold() - goldSave);
        String signe=ANSI_RED+"";
        if(showGold>0)
            signe=ANSI_GREEN+"+";
        String res = ANSI_CYAN+name+ANSI_RESET + " (" + ANSI_ITALIC +role+ANSI_RESET + ") possede " + ANSI_YELLOW+getGold()+ANSI_RESET
                + "("+signe +showGold +ANSI_RESET + ") pieces d'or";
        if (!isNull(checkDraw))
            res += ", a pioché "+ANSI_UNDERLINE+checkDraw.getName()+ANSI_RESET;
        if (!isNull(checkBuilding))
            res += " et a construit " +ANSI_BOLD+checkBuilding.getName()+ANSI_RESET;
        System.out.println(res);
    }

    void takeCrown() {
        crown = true;
    }

    void leaveCrown() {
        crown = false;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(ANSI_PURPLE + name + ANSI_RESET + " Or : " + gold);
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

    public boolean getCrown() {
        return crown;
    }
}