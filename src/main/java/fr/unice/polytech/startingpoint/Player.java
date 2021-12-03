package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.Prestige;
import fr.unice.polytech.startingpoint.characters.Character;

import javax.swing.text.html.Option;
import java.util.*;

import static fr.unice.polytech.startingpoint.Main.*;
import static java.util.Objects.isNull;

public class Player {
    private final String name;
    private int gold;
    private int goldScore;
    private final Board board;
    private Optional<Character> role = Optional.empty();
    private boolean crown = false;
    private int nbBuildable = 1;
    private int taxes;
    private final Strategies strat;
    private final List<Building> cardHand;
    private final List<Building> city;


    Player(Board b, String name, Strategies strat) {
        this.name = name;
        board = b;
        gold = board.getBank().withdrawGold(2);
        cardHand = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            cardHand.add(board.getPile().drawACard().get());
        }
        city = new ArrayList<>();
        goldScore = 0;
        taxes = 0;
        this.strat = strat;
    }

    Player(Board b) {
        this.name = "undefined";
        board = b;
        gold = board.getBank().withdrawGold(2);
        cardHand = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            cardHand.add(board.getPile().drawACard().get());
        }
        city = new ArrayList<>();
        goldScore = 0;
        strat = Strategies.balanced;
    }

    void build(Building b) {
        boolean buildable = isBuildable(b);
        if (buildable) {
            board.getBank().refundGold(b.getCost());
            gold -= b.getCost();
            goldScore += b.getCost();
            cardHand.remove(b);
            city.add(b);
        }
    }

    boolean isBuildable(Building b) {
        return gold >= b.getCost() && !city.contains(b);
    }

    void play() {
        int goldDraw = getGold();
        boolean draw = !board.getPile().isEmpty() && cardHand.stream().allMatch(this::isBuildable);
        ArrayList<Building> checkBuilding = new ArrayList<>();
        Building checkDraw = null;
        if (getRole().orElse(null).gotMurdered()) {
            System.out.println(ANSI_ITALIC + getName() + " has been killed. Turn is skipped." + ANSI_RESET);
        } else {
            // chooses to draw a card because there is nothing buildable or bank is empty
            if (draw || board.getBank().isEmpty())
                checkDraw = drawDecision().orElse(null);
                // chooses to get 2 golds because nothing can be built
            else
                gold += board.getBank().withdrawGold(2);
            getRole().orElse(null).usePower(this);
            //TODO Board ?
            int goldTaxes = getGold();
            gold += board.getBank().withdrawGold(taxes);
            goldTaxes = getGold() - goldTaxes;

            getCity().forEach(e -> {
                if(e instanceof Prestige)
                    ((Prestige) e).useEffect(this);
            } );
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
                cardHand.add(x);
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
        for (Building b : getCardHand()) {
            if (isBuildable(b) && nbBuildable > 0) {
                if ((b.getCost() <= costMax && b.getCost() >= costMin)
                        || (board.getPile().isEmpty() && board.getBank().getGold() == 0)) {
                    nbBuildable--;
                    checkBuilding.add(b);
                }
            }
        }
        //TODO Iterator
        for(Building build : checkBuilding)
            build(build);
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
        else if(getCardHand().contains(b1))
            return b2;
        else if(getCardHand().contains(b2))
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
        role = Optional.of(board.getCharactersInfos(index));
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
        StringBuilder res = new StringBuilder(ANSI_PURPLE + name + "  " + role + ANSI_RESET +
                " avec la stratégie " + ANSI_RED + strat + ANSI_RESET +
                " et " + ANSI_YELLOW + gold + ANSI_RESET + " pieces d'or");
        res.append("\nBâtiments :").append("\tScore des Bâtiments : ").append(ANSI_CYAN_BACKGROUND).append(ANSI_BLACK).append(goldScore).append(ANSI_RESET);
        for (Building b : city) {
            res.append("\n\t").append(b.toString()).append(" ");
        }
        return res.toString();
    }

    public int getGold() {
        return gold;
    }

    int getGoldScore() {
        return goldScore;
    }

    String getName() {
        return name;
    }

    Optional<Character> getRole() {
        return role;
    }

    public List<Building> getCardHand() {
        return cardHand;
    }

    boolean getCrown() {
        return crown;
    }

    void setCrown(boolean c) {
        crown = c;
    }

    public void setNbBuildable(int number) {
        nbBuildable = number;
    }

    public void setTaxes(int number) {
        taxes = number;
    }

    public List<Building> getCity() {
        return city;
    }

    public void drawCards(int nbCards) {
        for (int i = 0; i < nbCards; i++) {
            Optional<Building> b1 = board.getPile().drawACard();
            b1.ifPresent(cardHand::add);
        }
    }

    public int getTaxes() {
        return taxes;
    }

    //TODO Changer methode atttribution Role
    public void setRole(int number) {
        role = Optional.of(board.getCharactersInfos(number));
        board.getCharactersInfos(number).isTaken();
    }

    void removeRole() {
        role = Optional.empty();
    }

    public Board getBoard() {
        return board;
    }

    public void discardCard(Optional<Building> buildingOptional) {
        if (!getCardHand().isEmpty()) {
            Building b = buildingOptional.orElse(getCardHand().get(0));
            cardHand.remove(b);
            board.getPile().putCard(b);
        }
    }

    public int getNbBuildable() {
        return nbBuildable;
    }

    public static Comparator<Player> RoleOrder = (e1, e2) -> {
        //Positive if e2 > e1
        int res = 1;
        if (e1.getRole().isPresent() && e2.getRole().isPresent()) {
            res = e1.getRole().get().getOrder() - e2.getRole().get().getOrder();
        } else if (e1.getRole().isPresent())
            res = -1;
        return res;
    };

    public void refundGold(int i) {
        gold -= i;
        board.getBank().refundGold(i);
    }
}
