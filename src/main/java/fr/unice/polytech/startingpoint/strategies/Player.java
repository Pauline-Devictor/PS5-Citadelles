package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.*;
import fr.unice.polytech.startingpoint.characters.Character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static fr.unice.polytech.startingpoint.Board.*;
import static java.util.Objects.isNull;

public class Player implements Comparator<Building> {
    protected String name;
    protected int gold;
    protected int goldScore;
    protected final Board board;
    protected Character role;
    protected int nbBuildable = 1;
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
        role = null;
    }

    public static Comparator<Player> RoleOrder = (e1, e2) -> {
        //Positive if e2 > e1
        int res = 1;
        if (!isNull(e1.getRole()) && !isNull(e2.getRole())) {
            res = e1.getRole().getOrder() - e2.getRole().getOrder();
        } else if (isNull(e1.getRole()))
            res--;
        return res;
    };

    public static Comparator<Player> PointsOrder = (e1, e2) -> {
        //Positive if e2 > e1
        return e2.getGoldScore() - e1.getGoldScore();
    };

    public void build(Building b) {
        if (isBuildable(b)) {
            refundGold(b.getCost());
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
        if (getRole().isMurdered()) {
            System.out.println(ANSI_ITALIC + getName() + " has been killed. Turn is skipped.\n" + ANSI_RESET);
        } else {
            checkStolen();
            //Decide for the use of power of his character
            roleEffects();
            // chooses to draw a card because there is nothing buildable or bank is empty
            if (draw)
                drawDecision();
                // chooses to get 2 golds because nothing can be built
            else
                gold += board.getBank().withdrawGold(2);
            //Decide for the use of wonders
            cityEffects();
            //Decide what to build
            buildDecision();
            //show the move in the console
            board.showPlay(this, goldDraw);
        }
    }

    private boolean drawOrGold() {
        boolean emptyDeck = board.getPile().isEmpty();
        boolean anythingBuildable = cardHand.stream().anyMatch(this::isBuildable);
        boolean emptyBank = board.getBank().isEmpty();
        boolean isDraw = (!emptyDeck && !anythingBuildable) || emptyBank;
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
        getRole().usePower(board);
    }

    void checkStolen() {
        if (role.isStolen()) {
            int save = gold;
            refundGold(gold);
            role.getThief().takeMoney(save);
        }
    }

    public void buildDecision() {
        List<Building> checkBuilding = new ArrayList<>(getCardHand());
        checkBuilding.sort(this);

        List<Building> toBuild = new ArrayList<>();
        for (Building b : checkBuilding) {
            if (isBuildable(b) && nbBuildable > 0) {
                toBuild.add(b);
                build(b);
                nbBuildable--;
            }

        }
        board.showBuilds(checkBuilding, toBuild, name);
    }

    public void drawDecision() {
        if (getCity().containsAll(List.of(new Library(), new Observatory()))) {
            drawAndChoose(3, 2);
        } else if (getCity().contains(new Library())) {
            new Library().useEffect(this);
        } else if (getCity().contains(new Observatory())) {
            new Observatory().useEffect(this);
        } else {
            drawAndChoose(2, 1);
        }
    }

    public void drawAndChoose(int nbCards, int nbChoose) {
        List<Building> builds = new ArrayList<>();
        Optional<Building> b1;
        for (int i = 0; i < nbCards; i++) {
            b1 = getBoard().getPile().drawACard();
            b1.ifPresent(builds::add);
        }
        chooseBuilding(builds, nbChoose);
    }

    public void chooseBuilding(List<Building> builds, int nbBuilds) {

        builds.sort(this);
        Set<Building> discarded = new HashSet<>();
        for (Building b : builds) {
            if (getCardHand().contains(b) || getCity().contains(b)) {
                discarded.add(b);
            }
        }
        for (int i = nbBuilds; i < builds.size(); i++) {
            discarded.add(builds.get(i));
        }
        //On remet dans le deck les cartes non utilisés
        discarded.forEach(board::putCard);

        List<Building> builded = (builds.size() >= nbBuilds) ? builds.subList(0, nbBuilds) : builds;
        board.showDrawChoice(builds, new ArrayList<>(discarded), builded, name);
    }

    public void chooseRole() {
        int index;
        Random r = new Random();
        do {
            index = r.nextInt(8);
        } while (!board.getCharactersInfos(index).isAvailable());
        pickRole(index);
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

    public void refundGold(int amount) {
        gold -= board.getBank().refundGold(amount);
    }

    public void takeMoney(int amount) {
        gold += board.getBank().withdrawGold(amount);
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
            role = board.getCharactersInfos(index);
            role.took();
        }
        return b;
    }

    @Override
    public int compare(Building b1, Building b2) {
        //Positive if o2>o1
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(ANSI_PURPLE + name).append(", ").append(role);
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

    public Character getRole() {
        return role;
    }

    public List<Building> getCardHand() {
        return cardHand;
    }

    public void reset() {
        nbBuildable = 1;
    }

    public List<Building> getCity() {
        return city;
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

    public void buildingArchitect() {
        nbBuildable = 3;
    }
}
