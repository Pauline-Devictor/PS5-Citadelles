package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.*;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.*;

import java.util.*;

import static fr.unice.polytech.startingpoint.Board.*;

public class Player implements Comparator<Building> {
    protected String name;
    protected int gold;
    protected int goldScore;
    protected final Board board;
    //TODO Ecraser la valeur/Faire sauter Optional
    protected Optional<Character> role;
    protected int nbBuildable = 1;
    protected int taxes;
    protected List<Building> cardHand;
    protected final List<Building> city;

    public Player(Board b, String name) {
        this(b);
        this.name = name;
    }

    public Player(Board b) {
        this.name = "undefined";
        board = b;
        gold = board.getBank().withdrawGold(2);
        cardHand = new ArrayList<>();
        drawCards(4);
        city = new ArrayList<>();
        goldScore = 0;
        //amountStolen = 0;
        role = Optional.empty();
    }

    public static Comparator<Player> RoleOrder = (e1, e2) -> {
        //Positive if e2 > e1
        int res = 1;
        if (e1.getRole().isPresent() && e2.getRole().isPresent()) {
            res = e1.getRole().get().getOrder() - e2.getRole().get().getOrder();
        } else if (e1.getRole().isPresent())
            res--;
        return res;
    };

    public static Comparator<Player> PointsOrder = (e1, e2) -> {
        //Positive if e2 > e1
        return e2.getGoldScore() - e1.getGoldScore();
    };

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
        boolean draw = drawOrGold();
        List<Building> checkDraw = new ArrayList<>();
        if (getRole().isPresent() && getRole().get().isMurdered()) {
            System.out.println(ANSI_ITALIC + getName() + " has been killed. Turn is skipped." + ANSI_RESET);
        } else {
            checkStolen();
            //Decide for the use of power of his character
            roleEffects();
            // chooses to draw a card because there is nothing buildable or bank is empty
            if (draw)
                /*checkDraw = */ drawDecision();
                // chooses to get 2 golds because nothing can be built
            else
                gold += board.getBank().withdrawGold(2);
            //Decide for the use of wonders
            cityEffects();
            //Decide what to build
            List<Building> checkBuilding = buildDecision();
            //show the move in the console
            board.showPlay(this, goldDraw, checkDraw, checkBuilding);
        }
    }

    private boolean drawOrGold() {
        boolean emptyDeck = board.getPile().isEmpty();
        boolean anythingBuildable = cardHand.stream().anyMatch(this::isBuildable);
        boolean emptyBank = board.getBank().isEmpty();
        boolean isDraw = (!emptyDeck && anythingBuildable) || emptyBank;
        board.showDrawOrGold(emptyDeck, anythingBuildable, emptyBank, isDraw, getName());
        return isDraw;
    }

    public void cityEffects() {
        getCity().forEach(e -> {
            if (e instanceof Laboratory || e instanceof Manufactory) {
                ((Prestige) e).useEffect(this);
            }
        });
    }

    public void roleEffects() {
        if (getRole().isPresent()) {
            getRole().get().usePower(board);
        }
    }

    void checkStolen() {
        if (getRole().isPresent()) {
            Optional<Player> thief = getRole().get().getThief();
            if (thief.isPresent()) {
                int save = gold;
                refundGold(gold);
                thief.get().takeMoney(save);
            }
        }
    }

    public List<Building> buildDecision() {
        List<Building> checkBuilding = new ArrayList<>(getCardHand());
        checkBuilding.sort(this);

        List<Building> toBuild = new ArrayList<>();
        for (Building b : checkBuilding) {
            if (isBuildable(b) && nbBuildable > 0) {
                toBuild.add(b);
                nbBuildable--;
            }
        }
        return toBuild;
    }

    public /*List<Building>*/ void drawDecision() {
        List<Building> res, tmp;
        if (getCity().containsAll(List.of(new Library(), new Observatory()))) {
            //TODO Combiné Batiments Effects + Tests
            res = drawAndChoose(3, 2);
        } else if (getCity().contains(new Library())) {
            //tmp = List.copyOf(getCardHand());
            new Library().useEffect(this);
            //res = cardHand;
            //res.removeAll(tmp);
        } else if (getCity().contains(new Observatory())) {
            //tmp = List.copyOf(getCardHand());
            new Observatory().useEffect(this);
            //res = cardHand;
            //res.removeAll(tmp);
        } else {
            res = drawAndChoose(2, 1);
        }
        //return res;
    }

    public List<Building> drawAndChoose(int nbCards, int nbChoose) {
        List<Building> builds = new ArrayList<>();
        Optional<Building> b1;
        for (int i = 0; i < nbCards; i++) {
            b1 = getBoard().getPile().drawACard();
            b1.ifPresent(builds::add);
        }
        board.showDrawChoice();
        return chooseBuilding(builds, nbChoose);
    }

    public List<Building> chooseBuilding(List<Building> builds, int res) {
        builds.sort(this);
        builds.removeIf(b -> getCardHand().contains(b) || getCity().contains(b));
        return (builds.size() >= res) ? builds.subList(0, res) : builds;
    }

    public void chooseRole() {
        int index;
        do {
            index = new Random().nextInt(8);
        } while (!board.getCharactersInfos(index).isAvailable());
        role = Optional.of(board.getCharactersInfos(index));
        board.getCharactersInfos(index).setAvailable(false);
    }

    public List<Building> drawCards(int nbCards) {
        List<Building> res = new ArrayList<>();
        for (int i = 0; i < nbCards; i++) {
            Optional<Building> b1 = board.getPile().drawACard();
            b1.ifPresent(cardHand::add);
            b1.ifPresent(res::add);
        }
        return res;
    }

    public Building discardCard() {
        if (getCardHand().size() > 0) {
            Building b = cardHand.get(0);
            cardHand.remove(b);
            board.getPile().putCard(b);
            return b;
        }
        return null;
    }

    public void discardCard(Building b) {
        if (getCardHand().size() > 0) {
            cardHand.remove(b);
            board.getPile().putCard(b);
        }
    }

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
            res.append(", ").append(role);
        res.append(ANSI_RESET + " avec " + ANSI_YELLOW).append(gold).append(ANSI_RESET).append(" pieces d'or");
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

    public void setRole(Optional<Character> role) {
        this.role = role;
    }

    public District getMajority() {
        HashMap<District, Integer> majority = new HashMap<>();
        for (District d : District.values()) {
            majority.put(d, 0);
        }
        for (Building b : getCity()) {
            majority.put(b.getDistrict(), majority.get(b.getDistrict()) + 1);
        }
        return Collections.max(majority.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    public boolean pickRole(int index) {
        boolean b = board.getCharactersInfos(index).isAvailable();
        if (b) {
            role = Optional.of(board.getCharactersInfos(index));
            board.getCharactersInfos(index).setAvailable(false);
        }
        return b;
    }

    @Override
    public int compare(Building b1, Building b2) {
        //Positive if o2>o1
        return 0;
    }
}
