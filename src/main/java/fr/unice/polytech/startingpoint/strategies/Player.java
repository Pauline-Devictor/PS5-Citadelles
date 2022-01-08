package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.*;
import fr.unice.polytech.startingpoint.characters.Character;

import java.util.*;
import java.util.stream.Collectors;

import static fr.unice.polytech.startingpoint.Display.*;
import static java.util.Objects.isNull;

/**
 * Class that represents a single player of the game with its golds, city, hand, role and score.
 * All decisions made by a player is either done by this class or one of its subclasses.
 */
public class Player implements Comparator<Building> {
    protected final String name;
    protected int gold;
    protected int score;
    protected final Board board;
    protected Character role;
    protected int nbBuildable = 1;
    protected List<Building> cardHand;
    protected final List<Building> city;

    /**
     * Instantiates a new Player.
     *
     * @param b    board linked to the player
     * @param name player's name
     *             Creates a player linked to a board with a name
     */
    public Player(Board b, String name) {
        this.name = this.getClass().getSimpleName() + "-" + name;
        board = b;
        gold = board.getBank().withdrawGold(2);
        cardHand = new ArrayList<>();
        city = new ArrayList<>();
        drawAndChoose(4, 4);
        score = 0;
        role = null;
    }

    /**
     * Instantiates a new Player.
     *
     * @param b board linked to the player Creates a player linked to a board with undefined name
     */
    public Player(Board b) {
        this.name = this.getClass().getSimpleName() + "-X";
        board = b;
        gold = board.getBank().withdrawGold(2);
        cardHand = new ArrayList<>();
        city = new ArrayList<>();
        drawAndChoose(4, 4);
        score = 0;
        role = null;
    }

    /**
     * Compare two players depending on the order in the turn
     */
    public static final Comparator<Player> RoleOrder = (e1, e2) -> {
        //Positive if e2 > e1
        int res = 1;
        if (!isNull(e1.getRole()) && !isNull(e2.getRole())) {
            res = e1.getRole().getOrder() - e2.getRole().getOrder();
        } else if (isNull(e1.getRole()))
            res--;
        return res;
    };

    /**
     * The constant PointsOrder.
     */
    public static final Comparator<Player> PointsOrder = (e1, e2) -> {
        //Positive if e2 > e1
        return e2.getScore() - e1.getScore();
    };

    /**
     * Build b is possible
     *
     * @param b Building to build
     */
    public void build(Building b) {
        if (isBuildable(b)) {
            refundGold(b.getCost());
            score += b.getCost();
            cardHand.remove(b);
            city.add(b);
        }
    }

    /**
     * Is buildable boolean.
     *
     * @param b the b
     * @return the boolean
     */
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
        showPlay(this, goldDraw);
    }

    /**
     * Draw or gold boolean.
     *
     * @return boolean, true is the player should draw, false if the player should get golds
     */
    public boolean drawOrGold() {
        boolean emptyDeck = getBoard().getPile().isEmpty();
        boolean anythingBuildable = getCardHand().stream().anyMatch(this::isBuildable);
        boolean emptyBank = getBoard().getBank().isEmpty();
        boolean isDraw = (!emptyDeck && !anythingBuildable) || emptyBank;
        showDrawOrGold(emptyDeck, anythingBuildable, emptyBank, isDraw, this);
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
        showBuilds(checkBuilding, toBuild, this);
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
     * Draw and choose list.
     *
     * @param nbCards  number of cards to choose from
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
     * Choose building list.
     *
     * @param builds   list of buildings drawn
     * @param nbBuilds numbers of buildings to choose
     * @return List of chosen cards make the player choose which cards to pick whenever he draws
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
        showDrawChoice(builds, new ArrayList<>(discarded), drawn, this);
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
     * Discard card building.
     *
     * @return the building discarded, or null
     */
    public Building discardCard() {
        if (getCardHand().size() > 0) {
            cardHand.sort(this);
            Collections.reverse(cardHand);
            Building b = cardHand.get(0);
            cardHand.remove(b);
            board.getPile().putCard(b);
            return b;
        }
        return null;
    }

    /**
     * Refund gold.
     *
     * @param amount amount to give back
     */
    public void refundGold(int amount) {
        gold -= board.getBank().refundGold(
                Math.min(amount, getGold())
        );
    }

    /**
     * Take money.
     *
     * @param amount amount of golds to borrow gets golds from bank
     */
    public void takeMoney(int amount) {
        gold += board.getBank().withdrawGold(amount);
    }

    /**
     * Gets majority.
     *
     * @return the district that has the most building of its kind in current player's city
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
     * Pick role boolean.
     *
     * @param index index of the given role (listed by turn order from 0)
     * @return true if the given role was available, else false
     */
    public boolean pickRole(int index) {
        boolean b = board.getCharactersInfos(index).isAvailable();
        if (b) {
            role = board.getCharactersInfos(index);
            role.took();
            showRole(this);
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
        return printName(this) + ", " + (isNull(getRole()) ? "" : printRole(this)) +
                printFormat(" avec ", ANSI_WHITE) + printFormat(String.valueOf(gold), ANSI_YELLOW, ANSI_BOLD) + printFormat(" pieces d'or et un score de ", ANSI_WHITE) +
                printFormat(String.valueOf(score), ANSI_BLUE) + "\n" +
                printFormat("Bâtiments Non Construits :", ANSI_BLUE_BACKGROUND, ANSI_BLACK) +
                printBuildings(cardHand, true) +
                "\n" + printFormat("Bâtiments Construits :", ANSI_CYAN_BACKGROUND, ANSI_BLACK) +
                printBuildings(city, true);
    }

    /**
     * compute boolean in oder to do if bonus are available
     * 0 : Districts
     * 1 : Prestige
     * 2 : Complete City
     *
     * @return an array of boolean for bonus points
     */
    public boolean[] calculBonus() {
        boolean districts = getCity().stream().map(Building::getDistrict).distinct().count() == 5;
        boolean prestiges = getCity().stream().filter(e -> e.getDistrict() == District.Prestige).count() >= 2
                && getCity().stream().anyMatch(e -> e.equals(new MiracleCourtyard()));
        boolean cityDone = getCity().size() >= 8;
        return new boolean[]{districts, prestiges, cityDone};
    }

    /**
     * modify the score depending on the bonus of the player
     *
     * @param first is the player first to finish
     */
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

    /**
     * Gets gold.
     *
     * @return the gold
     */
    public int getGold() {
        return gold;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public Character getRole() {
        return role;
    }

    /**
     * Gets card hand.
     *
     * @return the card hand
     */
    public List<Building> getCardHand() {
        return cardHand;
    }

    /**
     * Reset.
     */
    public void reset() {
        nbBuildable = 1;
    }

    /**
     * Gets city.
     *
     * @return the city
     */
    public List<Building> getCity() {
        return city;
    }


    /**
     * Gets board.
     *
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets nb buildable.
     *
     * @return the number of buildable builds
     */
    public int getNbBuildable() {
        return nbBuildable;
    }

    /**
     * Sets card hand.
     *
     * @param cards the cards
     */
    public void setCardHand(List<Building> cards) {
        cardHand = cards;
    }

    /**
     * Set Number of Builds to 3.
     */
    public void buildingArchitect() {
        nbBuildable = 3;
    }

    /**
     * Gave i Bonus points.
     *
     * @param i the number of points
     */
    public void bonusPoints(int i) {
        score += i;
    }

    protected TreeMap<District, Integer> getDistrictValues() {
        TreeMap<District, Integer> taxmap = new TreeMap<>();
        for (District d : District.values()) {
            taxmap.put(d, 0);
        }
        for (Building b : getCity()) {
            taxmap.put(b.getDistrict(), taxmap.get(b.getDistrict()) + 1);
        }
        return taxmap;
    }

    protected boolean pickRole(List<Integer> taxList) {
        for (int elem : taxList) {
            if (pickRole(elem)) {
                return true;
            }
        }
        return false;
    }

    protected ArrayList<Integer> mapToSortedList(TreeMap<District, Integer> taxmap) {
        ArrayList<Integer> taxList = (ArrayList<Integer>) taxmap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(e -> e.getKey().getTaxCollector())
                .collect(Collectors.toList());
        Collections.reverse(taxList);
        return taxList;
    }

    public int compareRushDistrict(District d, Building b1, Building b2, int costMin, int costMax) {
        int res = 0;
        if (b1.getCost() <= costMax && b1.getCost() >= costMin && b2.getCost() <= costMax && b2.getCost() >= costMin) {
            if (b1.getDistrict() == d) res--;
            if (b2.getDistrict() == d) res++;
            if (b1.getDistrict() == d && b2.getDistrict() == d) res = (b1.getCost() - b2.getCost());
        }
        return res;
    }
}
