package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.*;
import fr.unice.polytech.startingpoint.characters.Character;

import java.util.*;

import static fr.unice.polytech.startingpoint.Board.*;
import static java.util.Objects.isNull;

/**
 * Class that represents a single player of the game with its golds, city, hand, role and score.
 * All decisions made by a player is either done by this class or one of its subclasses.
 */
public class Player implements Comparator<Building> {
    protected String name;
    protected int gold;
    protected int score;
    protected final Board board;
    protected Character role;
    protected int nbBuildable = 1;
    protected List<Building> cardHand;
    protected final List<Building> city;

    /**
     * @param b board linked to the player
     * @param name player's name
     * Creates a player linked to a board with a name
     */
    public Player(Board b, String name) {
        this.name = name;
        board = b;
        gold = board.getBank().withdrawGold(2);
        cardHand = new ArrayList<>();
        city = new ArrayList<>();
        drawAndChoose(4, 4);
        score = 0;
        role = null;
    }

    /**
     * @param b board linked to the player
     * Creates a player linked to a board with undefined name
     */
    public Player(Board b) {
        this.name = "undefined";
        board = b;
        gold = board.getBank().withdrawGold(2);
        cardHand = new ArrayList<>();
        city = new ArrayList<>();
        drawAndChoose(4, 4);
        score = 0;
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
        return e2.getScore() - e1.getScore();
    };

    public void build(Building b) {
        if (isBuildable(b)) {
            refundGold(b.getCost());
            score += b.getCost();
            cardHand.remove(b);
            city.add(b);
        }
    }

    public boolean isBuildable(Building b) {
        return gold >= b.getCost() && !city.contains(b);
    }

    /**
     * Make the player play a single turn
     */
    public void play() {
        int goldDraw = getGold();
        if (!getRole().isMurdered()) {
            boolean draw = drawOrGold();
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
        }
        board.showPlay(this, goldDraw);
    }

    /**
     * @return boolean, true is the player should draw, false if the player should get golds
     */
    public boolean drawOrGold() {
        boolean emptyDeck = getBoard().getPile().isEmpty();
        boolean anythingBuildable = getCardHand().stream().anyMatch(this::isBuildable);
        boolean emptyBank = getBoard().getBank().isEmpty();
        boolean isDraw = (!emptyDeck && !anythingBuildable) || emptyBank;
        board.showDrawOrGold(emptyDeck, anythingBuildable, emptyBank, isDraw, this);
        return isDraw;
    }

    /**
     * plays Prestige city effects each turn
     */
    public void cityEffects() {
        getCity().forEach(e -> {
            if (e instanceof Laboratory || e instanceof Manufactory) {
                ((Prestige) e).useEffect(this);
            }
        });

    }

    /**
     * plays Prestige city effects that apply at the end of the game
     */
    public void cityEffectsEnd() {
        getCity().forEach(e -> {
            if (e instanceof Dracoport || e instanceof University) {
                ((Prestige) e).useEffect(this);
            }
        });
    }

    /**
     * Uses player's current role power
     */
    private void roleEffects() {
        getRole().usePower(board);
    }


    /**
     * Activates Thief's power if the current player has been targeted
     */
    private void checkStolen() {
        if (role.isStolen()) {
            int save = gold;
            refundGold(gold);
            role.getThief().takeMoney(save);
        }
    }

    /**
     * make the player build as many buildings as possible
     */
    private void buildDecision() {
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
        board.showBuilds(checkBuilding, toBuild, this);
    }

    /**
     * Applies Prestige effects linked to drawing
     */
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

    /**
     * @param nbCards number of cards to choose from
     * @param nbChoose number of cards to pick
     * @return List of chosen cards
     */
    public List<Building> drawAndChoose(int nbCards, int nbChoose) {
        List<Building> builds = new ArrayList<>();
        Optional<Building> b1;
        for (int i = 0; i < nbCards; i++) {
            b1 = getBoard().getPile().drawACard();
            b1.ifPresent(builds::add);
        }
        return chooseBuilding(builds, nbChoose);
    }

    /**
     * @param builds list of buildings drawn
     * @param nbBuilds numbers of buildings to choose
     * @return List of chosen cards
     * make the player choose which cards to pick whenever he draws
     */
    public List<Building> chooseBuilding(List<Building> builds, int nbBuilds) {
        nbBuilds = (Math.max(nbBuilds, 0));
        builds.sort(this);
        Set<Building> discarded = new HashSet<>();
        for (int i = nbBuilds; i < builds.size(); i++) {
            discarded.add(builds.get(i));
        }
        //On remet dans le deck les cartes non utilisés
        discarded.forEach(board::putCard);

        List<Building> drawn = (builds.size() >= nbBuilds) ? builds.subList(0, nbBuilds) : builds;
        cardHand.addAll(drawn);
        board.showDrawChoice(builds, new ArrayList<>(discarded), drawn, this);
        return drawn;
    }

    /**
     * make the player choose an available role
     */
    public void chooseRole() {
        int index;
        Random r = new Random();
        do {
            index = r.nextInt(8);
        } while (!board.getCharactersInfos(index).isAvailable());
        pickRole(index);
    }

    /**
     * @return true if a card has been discarded, else false
     * discards first card of the hand
     */
    public Building discardCard() {
        if (getCardHand().size() > 0) {
            Building b = cardHand.get(0);
            cardHand.remove(b);
            board.getPile().putCard(b);
            return b;
        }
        return null;
    }

    /**
     * @param amount amount to give back
     * give back golds to bank
     */
    public void refundGold(int amount) {
        gold -= board.getBank().refundGold(
                Math.min(amount, getGold())
        );
    }

    /**
     * @param amount amount of golds to borrow
     * gets golds from bank
     */
    public void takeMoney(int amount) {
        gold += board.getBank().withdrawGold(amount);
    }

    /**
     * @return the district that has the most building
     * of its kind in current player's city
     */
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

    /**
     * @param index index of the given role (listed by turn order from 0)
     * @return true if the given role was available, else false
     */
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
        if (getCardHand().containsAll(List.of(b1, b2)) || getCity().containsAll(List.of(b1, b2)))
            return 0;
        if (getCardHand().contains(b1) || getCity().contains(b1))
            return 1;
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(printName(this)).append(", ").append(board.printRole(this));

        res.append(ANSI_RESET + " avec ").append(printFormat(String.valueOf(gold), ANSI_YELLOW, ANSI_BOLD)).append(" pieces d'or et un score de ")
                .append(printFormat(String.valueOf(score), ANSI_BLUE)).append("\n")
                .append(printFormat("Bâtiments Non Construits :", ANSI_BLUE_BACKGROUND, ANSI_BLACK));
        res.append(printBuildings(cardHand, true));
        res.append("\n").append(printFormat("Bâtiments Construits :", ANSI_CYAN_BACKGROUND, ANSI_BLACK));
        res.append(printBuildings(city, true));
        return res.toString();
    }

    public boolean[] calculBonus() {
        boolean districts = getCity().stream().map(Building::getDistrict).distinct().count() == 5;
        boolean prestiges = getCity().stream().filter(e -> e.getDistrict() == District.Prestige).count() >= 2
                && getCity().stream().anyMatch(e -> e.equals(new MiracleCourtyard()));
        boolean cityDone = getCity().size() >= 8;
        return new boolean[]{districts, prestiges, cityDone};
    }

    public void calculScore(boolean first) {
        boolean[] var = calculBonus();
        if (var[0] || var[1])
            score += 3;
        if (var[2])
            score += 2;
        if (first)
            score += 2;
        cityEffectsEnd();
    }

    public int getGold() {
        return gold;
    }

    public int getScore() {
        return score;
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

    public void bonusPoints(int i) {
        score += i;
    }
}
