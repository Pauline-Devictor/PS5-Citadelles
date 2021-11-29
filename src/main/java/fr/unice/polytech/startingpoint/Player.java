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
    private int nbBuildable = 1;
    private int taxes;
    private final Strategies strat;


    Player(Board b, String name, Strategies strat) {
        this.name = name;
        board = b;
        gold = board.getBank().withdrawGold(2);
        buildings = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            buildings.add(board.getPile().drawACard());
        }
        goldScore = 0;
        taxes = 0;
        this.strat = strat;
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
        strat = Strategies.balanced;
    }

    void build(Building b) {
        boolean buildable = isBuildable(b);
        if (buildable) {
            board.getBank().refundGold(b.getCost());
            gold -= b.getCost();
            goldScore += b.getCost();
            b.build();
        }
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
        int goldDraw = getGold();
        boolean draw = !board.getPile().isEmpty() && buildings.stream().allMatch(this::alreadyBuilt);
        ArrayList<Building> checkBuilding = new ArrayList<>();
        Building checkDraw = null;
        if (getRole().gotMurdered()) {
            System.out.println(ANSI_ITALIC + getName() + " has been killed. Turn is skipped." + ANSI_RESET);
        } else {
            //TODO Code Propre ?

            // chooses to draw a card because there is nothing buildable
            if (draw)
                checkDraw = drawDecision();
                // chooses to get 2 golds because nothing can be built
            else {
                if (board.getBank().getGold() > 0) {
                    gold += board.getBank().withdrawGold(2);
                } else {
                    checkDraw = drawDecision();
                }
            }
            getRole().usePower();
            int goldTaxes = getGold();
            gold += board.getBank().withdrawGold(taxes);
            goldTaxes = getGold() - goldTaxes;
            //build firsts buildings available
            buildDecision(checkBuilding);
            showPlay(goldDraw, goldTaxes, checkDraw, checkBuilding);
        }

    }

    /*private Building drawDecision() {
        Building checkDraw;
        checkDraw = board.getPile().drawACard();
        if (!isNull(checkDraw))
            buildings.add(checkDraw);
        return checkDraw;
    }*/

    private Building drawDecision() {
        Building b1 = board.getPile().drawACard();
        if (!isNull(b1)) {
            Building b2 = board.getPile().drawACard();
            if (isNull(b2)){
                buildings.add(b1);
                return b1;
            }
            else{
                Building b = chooseBuilding(b1,b2);
                buildings.add(b);
                return b;
            }
        }
        return b1;
    }

    /**
     * Choose the best building according to the strategie of the player
      * @param b1 First Building to compare
     * @param b2 Second Building to compare
     */
    private Building chooseBuilding(Building b1,Building b2) {
        //TODO 1/2 Parties infinis, a verif aux tests
        if (buildings.contains(b1))
            return b2;
        if(buildings.contains(b2))
            return b1;
        return switch (strat){
            case lowGold -> b1.getCost()>b2.getCost() ? b1 : b2;
            case highGold -> b1.getCost()<b2.getCost() ? b1 : b2;
            default -> b1;
        };
    }

    private void buildDecision(ArrayList<Building> checkBuilding) {
        int costMin = 0;
        int costMax = 6;
        switch (strat) {
            case lowGold -> costMax = 3;
            case highGold -> costMin = 3;
        }
        for (Building b : buildings) {
            if (isBuildable(b) && nbBuildable > 0) {
                //TODO Modif Conditions
                if ((b.getCost() <= costMax && b.getCost() >= costMin)
                        || (board.getPile().isEmpty() && board.getBank().getGold() == 0)) {
                    build(b);
                    nbBuildable -= 1;
                    checkBuilding.add(b);
                }
            }
        }
    }

    private void showPlay(int goldDraw, int goldCollect, Building checkDraw, ArrayList<Building> checkBuilding) {
        int showGold = (getGold() - goldDraw);
        String signe = ANSI_RED + "";
        if (showGold > 0)
            signe = ANSI_GREEN + "+";
        StringBuilder res = new StringBuilder(ANSI_CYAN + name + ANSI_RESET + " (" + ANSI_ITALIC + strat + ", " + role + ANSI_RESET + ") possede " + ANSI_YELLOW + getGold() + ANSI_RESET
                + "(" + signe + showGold + ANSI_RESET + ") pieces d'or");
        if (!isNull(checkDraw))
            res.append(", a pioché " + ANSI_UNDERLINE).append(checkDraw.getName()).append(ANSI_RESET);
        if (checkBuilding.size() > 0) {
            res.append(" et a construit ");
            for (Building e : checkBuilding) {
                res.append(ANSI_BOLD).append(e.getName()).append(ANSI_RESET).append(", ");
            }
        }
        if (goldCollect > 0)
            res.append(" et a recupere ").append(goldCollect).append(" pieces des impôts");
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
        StringBuilder res = new StringBuilder(ANSI_PURPLE + name + ANSI_RESET +
                " avec la stratégie " + ANSI_RED + strat + ANSI_RESET +
                " et " + ANSI_YELLOW + gold + ANSI_RESET + " pieces d'or");
        res.append("\nBâtiments :").append("\tScore des Bâtiments : ").append(ANSI_CYAN_BACKGROUND).append(ANSI_BLACK).append(goldScore).append(ANSI_RESET);
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

    public void setNbBuildable(int number) {
        nbBuildable = number;
    }

    public Character chooseVictim() {
        Random random = new Random();
        int victim = random.nextInt(7) + 1;
        return board.getCharacters().get(victim);
    }

    public void setTaxes(int number) {
        taxes = number;
    }
}
