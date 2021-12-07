package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.Prestige;
import fr.unice.polytech.startingpoint.characters.Character;

import java.util.*;

import static fr.unice.polytech.startingpoint.Board.*;
import static java.util.Objects.isNull;

public class Player {
    protected final String name;
    protected int gold;
    protected int goldScore;
    protected final Board board;
    protected Optional<Character> role;
    protected int nbBuildable = 1;
    protected int taxes;
    protected List<Building> cardHand;
    protected final List<Building> city;

    public Player(Board b, String name) {
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
        role = Optional.empty();
    }

    public Player(Board b) {
        this.name = "undefined";
        board = b;
        gold = board.getBank().withdrawGold(2);
        cardHand = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            cardHand.add(board.getPile().drawACard().get());
        }
        city = new ArrayList<>();
        goldScore = 0;
        //amountStolen = 0;
        role = Optional.empty();
    }

    public void build(Building b) {
        boolean buildable = isBuildable(b);
        if (buildable) {
            board.getBank().refundGold(b.getCost());
            gold -= b.getCost();
            goldScore += b.getCost();
            cardHand.remove(b);
            city.add(b);
        }
    }

    public boolean isBuildable(Building b) {
        return gold >= b.getCost() && !city.contains(b);
    }

    public void play() {
        int goldDraw = getGold();
        boolean draw = !board.getPile().isEmpty() && cardHand.stream().allMatch(this::isBuildable);
        Building checkDraw = null;
        if (getRole().orElse(null).isMurdered()) {
            System.out.println(ANSI_ITALIC + getName() + " has been killed. Turn is skipped." + ANSI_RESET);
        } else {
            checkStolen();
            // chooses to draw a card because there is nothing buildable or bank is empty
            if (draw || board.getBank().isEmpty())
                checkDraw = drawDecision().orElse(null);
                // chooses to get 2 golds because nothing can be built
            else
                gold += board.getBank().withdrawGold(2);
            //Decide for the use of power of his character
            int goldTaxes = roleEffects();
            //Decide for the use of wonders
            cityEffects();
            //Decide what to build
            List<Building> checkBuilding = buildDecision();
            //show the move in the console
            board.showPlay(this, goldDraw, goldTaxes, checkDraw, checkBuilding);
        }

    }

    void cityEffects() {
        getCity().forEach(e -> {
            if (e instanceof Prestige)
                ((Prestige) e).useEffect(this);
        });
    }

    int roleEffects() {
        getRole().orElse(null).usePower(board);
        int goldTaxes = getGold();
        gold += board.getBank().withdrawGold(taxes);
        return getGold() - goldTaxes;
    }

    void checkStolen() {
        Optional<Player> thief = getRole().get().getThief();
        if (thief.isPresent()) {
            int save = gold;
            refundGold(gold);
            thief.get().takeMoney(save);
        }
    }

    Optional<Building> drawDecision() {
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

    public List<Building> buildDecision() {
        //Scale of cost ok for building
        return buildDecision(0, 6);
    }

    public List<Building> buildDecision(int costMin, int costMax) {
        List<Building> checkBuilding = new ArrayList<>();
        for (Building b : getCardHand()) {
            if (isBuildable(b) && nbBuildable > 0) {
                if ((b.getCost() <= costMax && b.getCost() >= costMin)
                        || (board.getPile().isEmpty() && board.getBank().getGold() == 0)) {
                    nbBuildable--;
                    checkBuilding.add(b);
                }
            }
        }
        checkBuilding.forEach(this::build);
        return checkBuilding;
    }

    /**
     * Choose the best building according to the strategies of the player
     *
     * @param b1 First Building to compare
     * @param b2 Second Building to compare
     */
    public Building chooseBuilding(Building b1, Building b2) {
        if (isNull(b1))
            return b2;
        else if (isNull(b2))
            return b1;
        else if (getCardHand().contains(b1))
            return b2;
        else if (getCardHand().contains(b2))
            return b1;
        return b1;
    }

    public Character chooseVictim() {
        Random random = new Random();
        int victim = random.nextInt(7) + 1;
        return board.getCharacters().get(victim);
    }

    public Optional<Player> chooseTarget() {
        Random random = new Random();
        int victim = random.nextInt(board.getPlayers().size());
        return Optional.of(board.getPlayers().get(victim));
    }

    public void chooseRole() {
        int index;
        do {
            index = new Random().nextInt(8);
        } while (!board.getCharactersInfos(index).isAvailable());
        role = Optional.of(board.getCharactersInfos(index));
        board.getCharactersInfos(index).setAvailable(false);
    }

    public void drawCards(int nbCards) {
        for (int i = 0; i < nbCards; i++) {
            Optional<Building> b1 = board.getPile().drawACard();
            b1.ifPresent(cardHand::add);
        }
    }

    public void discardCard(Optional<Building> buildingOptional) {
        if (!getCardHand().isEmpty()) {
            Building b = buildingOptional.orElse(getCardHand().get(0));
            cardHand.remove(b);
            board.getPile().putCard(b);
        }
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

    public static Comparator<Player> PointsOrder = (e1, e2) -> {
        //Positive if e2 > e1
        return e2.getGoldScore() - e1.getGoldScore();
    };

    public void refundGold(int amount) {
        gold -= board.getBank().refundGold(amount);
    }

    public void takeMoney(int amount) {
        gold += board.getBank().withdrawGold(amount);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(ANSI_PURPLE + name);
        if (role.isPresent())
            res.append(", " + role);
        res.append(ANSI_RESET + " avec " + ANSI_YELLOW + gold + ANSI_RESET + " pieces d'or");
        res.append("\nBâtiments :").append("\tScore des Bâtiments : ").append(ANSI_GREEN_BACKGROUND).append(ANSI_BLACK).append(goldScore).append(ANSI_RESET);
        res.append("\n" + ANSI_BLUE_BACKGROUND + ANSI_BLACK + "Batiments Non Construits :" + ANSI_RESET);
        for (Building b : cardHand) {
            res.append("\n\t").append(b.toString()).append(" ");
        }
        res.append("\n" + ANSI_CYAN_BACKGROUND + ANSI_BLACK + "Batiments Construits :" + ANSI_RESET);
        for (Building b : city) {
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

    public Optional<Character> getRole() {
        return role;
    }

    public List<Building> getCardHand() {
        return cardHand;
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


    public int getTaxes() {
        return taxes;
    }

    public void setRole(int number) {
        role = Optional.of(board.getCharactersInfos(number));
        board.getCharactersInfos(number).setAvailable(false);
    }

    public void removeRole() {
        role = Optional.empty();
    }

    public Board getBoard() {
        return board;
    }

    public int getNbBuildable() {
        return nbBuildable;
    }

    public void setCardHand(List<Building> cards) {
        cardHand = cards;
    }

}
