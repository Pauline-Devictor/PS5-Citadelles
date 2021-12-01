package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.Character;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
            buildings.add(board.getPile().drawACard().get());
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
            buildings.add(board.getPile().drawACard().get());
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

    void play() {
        int goldDraw = getGold();
        boolean draw = !board.getPile().isEmpty() && buildings.stream().allMatch(this::alreadyBuilt);
        ArrayList<Building> checkBuilding = new ArrayList<>();
        Building checkDraw = null;
        if (getRole().gotMurdered()) {
            System.out.println(ANSI_ITALIC + getName() + " has been killed. Turn is skipped." + ANSI_RESET);
        } else {
            // chooses to draw a card because there is nothing buildable or bank is empty
            if (draw || board.getBank().isEmpty())
                checkDraw = drawDecision().orElse(null);
            // chooses to get 2 golds because nothing can be built
            else
                gold += board.getBank().withdrawGold(2);
            getRole().usePower();
            //TODO Board ?
            int goldTaxes = getGold();
            gold += board.getBank().withdrawGold(taxes);
            goldTaxes = getGold() - goldTaxes;
            //build firsts buildings available
            buildDecision(checkBuilding);
            showPlay(goldDraw, goldTaxes, checkDraw, checkBuilding);
        }

    }

    private Optional<Building> drawDecision() {
        Optional<Building> b1 = board.getPile().drawACard();
        Optional<Building> b = b1;
        //Si la premiere Carte est nulle, la seconde aussi
        if (b1.isPresent()) {
            Optional<Building> b2 = board.getPile().drawACard();
            if (b2.isPresent()) {
                Building x = chooseBuilding(b1.get(), b2.get());
                buildings.add(x);
                b = Optional.of(x);
            }
        }
        return b;
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

    /**
     * Choose the best building according to the strategies of the player
     *
     * @param b1 First Building to compare
     * @param b2 Second Building to compare
     */
    Building chooseBuilding(Building b1, Building b2) {
        //TODO 1/3 Parties infinis, a verif aux tests
        if(isNull(b1))
            return b2;
        else if (isNull(b2))
            return b1;
        else if(getBuildings().contains(b1))
            return b2;
        else if(getBuildings().contains(b2))
            return b1;
        return switch (strat) {
            case lowGold -> (b1.getCost() < b2.getCost()) ? b1 : b2;
            case highGold -> (b1.getCost() > b2.getCost()) ? b1 : b2;
            default -> b1;
        };
    }

    public Character chooseVictim() {
        Random random = new Random();
        int victim = random.nextInt(7) + 1;
        return board.getCharacters().get(victim);
    }

    void chooseRole() {
        int index;
        do {
            index = new Random().nextInt(8);
        } while (!board.getCharactersInfos(index).isAvailable());
        role = board.getCharactersInfos(index);
        board.getCharactersInfos(index).isTaken();
    }

    //TODO Dans le Board ?
    void showPlay(int goldDraw, int goldCollect, Building checkDraw, ArrayList<Building> checkBuilding) {
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

    int getGold() {
        return gold;
    }

    int getGoldScore() {
        return goldScore;
    }

    String getName() {
        return name;
    }

    Character getRole() {
        return role;
    }

    public ArrayList<Building> getBuildings() {
        return (ArrayList<Building>) buildings.clone();
    }

    boolean getCrown() {
        return crown;
    }

    void takeCrown() {
        crown = true;
    }

    void leaveCrown() {
        crown = false;
    }

    public void setNbBuildable(int number) {
        nbBuildable = number;
    }

    public void setTaxes(int number) {
        taxes = number;
    }
    //For tests
    public void setRole(int number){
        role = board.getCharactersInfos(number);
        board.getCharactersInfos(number).isTaken();
    }
    //Architect
    public void draw2Cards(){
        Optional<Building> b1 = board.getPile().drawACard();
        Optional<Building> b2 = board.getPile().drawACard();
        b1.ifPresent(buildings::add);
        b2.ifPresent(buildings::add);
    }
}
