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
        boolean draw = !board.getPile().isEmpty() && cardHand.stream().allMatch(this::isBuildable);
        List<Building> checkDraw = new ArrayList<>();
        if (getRole().isPresent() && getRole().get().isMurdered()) {
            System.out.println(ANSI_ITALIC + getName() + " has been killed. Turn is skipped." + ANSI_RESET);
        } else {
            checkStolen();
            roleEffects();
            // chooses to draw a card because there is nothing buildable or bank is empty
            if (draw || board.getBank().isEmpty())
                checkDraw = drawDecision();
                // chooses to get 2 golds because nothing can be built
            else
                gold += board.getBank().withdrawGold(2);
            //Decide for the use of power of his character
            int goldTaxes = collectTaxes();
            //Decide for the use of wonders
            cityEffects();
            //Decide what to build
            List<Building> checkBuilding = buildDecision();
            //show the move in the console
            board.showPlay(this, goldDraw, goldTaxes, checkDraw, checkBuilding);
        }
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

    public int collectTaxes() {
        int goldTaxes = getGold();
        gold += board.getBank().withdrawGold(getTaxes());
        return getGold() - goldTaxes;
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

    public List<Building> drawDecision() {
        List<Building> res, tmp;
        if (getCity().containsAll(List.of(new Library(), new Observatory()))) {
            System.out.println("Effect Batiments Combinés");
            res = cardHand;
        } else if (getCity().contains(new Library())) {
            tmp = List.copyOf(getCardHand());
            new Library().useEffect(this);
            res = cardHand;
            res.removeAll(tmp);
        } else if (getCity().contains(new Observatory())) {
            tmp = List.copyOf(getCardHand());
            new Observatory().useEffect(this);
            res = cardHand;
            res.removeAll(tmp);
        } else {
            res = drawAndChoose(2, 1);
        }
        return res;
    }

    public List<Building> drawAndChoose(int nbCards, int nbChoose) {
        List<Building> builds = new ArrayList<>();
        Optional<Building> b1;
        for (int i = 0; i < nbCards; i++) {
            b1 = getBoard().getPile().drawACard();
            b1.ifPresent(builds::add);
        }
        return chooseBuilding(builds, nbChoose);
    }

    public List<Building> chooseBuilding(List<Building> builds, int res) {
        builds.sort(this);
        builds.removeIf(b -> getCardHand().contains(b) || getCity().contains(b));
        return builds.size() >= res ? builds.subList(0, res) : builds;
    }

    public Character chooseVictim() {
        if (role.isPresent()) {
            if (role.get().getClass() == Assassin.class) {
                if (cardHand.size() > 4) return new Magician();
                for (Player player : board.getPlayers()) {
                    if (player.getCity().size() > 5) {
                        District colour = getMajority(player);
                        switch (colour) {
                            case Commercial -> {
                                return new Merchant();
                            }
                            case Noble -> {
                                return new King();
                            }
                            case Military -> {
                                return new Condottiere();
                            }
                            case Religion -> {
                                return new Bishop();
                            }
                        }
                    }
                }
                Random random = new Random();
                int victim = random.nextInt(7) + 1;
                return board.getCharacters().get(victim);
            }
            if (role.get().getClass() == Thief.class) {
                Random random = new Random();
                //exclu l'indice de l'assassin
                int victim = random.nextInt(9) + 1;
                if (victim > 7) victim = 6;
                return board.getCharacters().get(victim);
            }
        }
        return null;
    }

    public Optional<Player> chooseTarget() {
        if (role.isPresent()) {
            if (role.get().getClass() == Magician.class) {
                Player biggestCity = board.getPlayers().get(0);
                if (cardHand.size() < 3) {
                    for (Player p : board.getPlayers()) {
                        if (p.getCity().size() > biggestCity.getCity().size()) biggestCity = p;
                    }
                    if (biggestCity.getCity().size() > 5) return Optional.of(biggestCity);
                }
                Player biggestHand;
                biggestHand = board.getPlayers().get(0);
                for (Player p : board.getPlayers()) {
                    if (p.getCardHand().size() > biggestHand.getCardHand().size()) biggestHand = p;
                }
                return Optional.ofNullable(biggestHand);
            }

            if (role.get().getClass() == Condottiere.class) {
                Player biggestCity;
                if (board.getPlayers().get(0).getRole().get().getClass() == Bishop.class) {
                    biggestCity = board.getPlayers().get(1);
                } else {
                    biggestCity = board.getPlayers().get(0);
                }
                for (Player p : board.getPlayers()) {
                    if (p.getRole().isPresent())
                        if (p.getCity().size() >= biggestCity.getCity().size() && (p.getRole().get().getClass() != Bishop.class))
                            biggestCity = p;
                }
                return Optional.ofNullable(biggestCity);
            }
        }
        return Optional.empty();
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

    public District getMajority(Player p) {
        HashMap<District, Integer> majority = new HashMap<>();
        for (District d : District.values()) {
            majority.put(d, 0);
        }
        for (Building b : p.getCity()) {
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

    public void chooseBuild(Optional<Player> playerTarget, Player condo) {
        if (playerTarget.isPresent()) {
            Random random = new Random();
            int nbBuild = playerTarget.get().getCity().size();
            if (nbBuild > 0) {
                int toDestroy = random.nextInt(nbBuild);
                //Retire le build de la liste des construit & ajoute le build au deck
                Building build = playerTarget.get().getCity().get(toDestroy);
                if (condo.getGold() >= build.getCost()) {
                    board.getPile().putCard(build);
                    playerTarget.get().getCity().remove(build);
                    condo.refundGold(build.getCost() - 1);
                    System.out.println("Le batiment " + build.getName() + " du joueur " + playerTarget.get().getName() + " a été détruit.");
                    //TODO affichage
                }

            }
        }
    }

    @Override
    public int compare(Building b1, Building b2) {
        //Positive if o2>o1
        return 1;
    }
}
