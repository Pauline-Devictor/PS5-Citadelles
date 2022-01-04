package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.Csv.CsvWrite;
import fr.unice.polytech.startingpoint.buildings.*;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.strategies.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static fr.unice.polytech.startingpoint.strategies.Player.PointsOrder;
import static java.util.Objects.isNull;

/**
 * The type Board.
 */
public class Board {

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_ITALIC = "\u001B[3m";
    public static final String ANSI_UNDERLINE = "\u001B[4m";

    private final Deck pile;
    private final Bank bank;
    private final List<Character> characters;
    private final List<Player> players;

    /**
     * Instantiates a new Board.
     *
     * @param nbPlayers the number of players
     */
    Board(int nbPlayers) {
        this.bank = new Bank(30);
        this.pile = new Deck();
        characters = List.of(new Assassin(),
                new Thief(),
                new Magician(),
                new King(),
                new Bishop(),
                new Merchant(),
                new Architect(),
                new Condottiere());
        players = generatePlayers(nbPlayers);
    }

    /**
     * Instantiates a new Board.
     */
    public Board() {
        this(4);
    }

    /**
     * Generate players list.
     *
     * @param nbPlayers the nb players
     * @return the list
     */
    public List<Player> generatePlayers(int nbPlayers) {
        List<Player> players = new ArrayList<>();
        players.add(new RushMerch(this));
        //players.add(new RushArchi(this));
        players.add(new HighScoreArchi(this));
        //players.add(new HighScoreThief(this));
        players.add(new HighThiefManufactory(this));
        players.add(new RushArchiLab(this));
        //players.add(new BalancedFirst(this));
        //players.add(new Player(this));
        for (int i = 4; i < nbPlayers; i++)
            players.add(new Player(this));
        return players;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Free every role and empty the list of the taken roles
     */
    void release() {
        characters.forEach(Character::resetRole);
        players.forEach(Player::reset);
    }

    /**
     * Gets pile.
     *
     * @return the pile
     */
    public Deck getPile() {
        return pile;
    }

    /**
     * Gets bank.
     *
     * @return the bank
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * @return List of every role
     */
    public List<Character> getCharacters() {
        return characters;
    }

    /**
     * Gets characters infos.
     *
     * @param index the index
     * @return the characters infos
     */
    public Character getCharactersInfos(int index) {
        if (index < 0)
            index = 0;
        else if (index > characters.size())
            index = characters.size() - 1;
        return getCharacters().get(index);
    } //TODO Find a better way

    /**
     * Print the turn of a player
     * @param p         the player
     * @param goldDraw  the gold at the beginning of the turn
     */
    public void showPlay(Player p, int goldDraw) {
        String res = printFormat("---------------------------------------------------------------", ANSI_WHITE, ANSI_BLACK_BACKGROUND);
        if (!isNull(p.getRole()) && p.getRole().isMurdered())
            res += "\n" + printName(p) + printFormat("a ete tué. Son tour est passé\n", ANSI_WHITE);
        else {
            int showGold = (p.getGold() - goldDraw);
            String signe = ANSI_RED + "";
            if (showGold > 0)
                signe = ANSI_GREEN + "+";
            res += "\n" + printName(p) + "(" + printRole(p) + ")" +
                    printFormat(" possède ", ANSI_WHITE)
                    + printFormat(String.valueOf(p.getGold()), ANSI_YELLOW, ANSI_BOLD)
                    + "(" + signe + showGold + ANSI_RESET
                    + printFormat(") pieces d'or" + ", ", ANSI_WHITE)
                    + printFormat(String.valueOf(p.getCardHand().size()), ANSI_CYAN, ANSI_BOLD)
                    + printFormat(" cartes et ", ANSI_WHITE)
                    + printFormat(String.valueOf(p.getCity().size()), ANSI_BOLD, ANSI_BLUE)
                    + printFormat(" batiments\n", ANSI_WHITE);
        }
        res += printFormat("---------------------------------------------------------------", ANSI_WHITE, ANSI_BLACK_BACKGROUND);
        LOGGER.fine(res);
        //System.out.println(res + "\n\n");
    }

    /**
     * Print the ranking of the players
     */
    void showRanking() {
        players.sort(PointsOrder);
        for (int i = 1; i <= players.size(); i++) {
            LOGGER.fine(printFormat(i + ". Avec ", ANSI_WHITE)
                    + printFormat(String.valueOf(players.get(i - 1).getScore()), ANSI_BOLD, ANSI_GREEN)
                    + printFormat(" points, c'est ", ANSI_WHITE)
                    + printName(players.get(i - 1))
            );
        }
    }

    /**
     * Show board.
     */
    void showBoard() {
        StringBuilder res = new StringBuilder();
        players.forEach(e -> res.append(e).append("\n"));
        LOGGER.fine(String.valueOf(res));
        //System.out.println(res);
    }

    /**
     * Show infos of the turn,where the golds are and the number of cards in the deck
     * @param turn the turn
     */
    void showVariables(int turn) {
        AtomicInteger res = new AtomicInteger();
        players.forEach(e -> res.addAndGet(e.getGold()));
        LOGGER.fine(printFormat("Tour " + turn, ANSI_BLACK, ANSI_RED_BACKGROUND)
                + printFormat(" Il reste ", ANSI_WHITE)
                + printFormat(String.valueOf(pile.numberOfCards()), ANSI_GREEN, ANSI_BOLD)
                + printFormat(" cartes dans la pioche.", ANSI_WHITE)
                + printFormat("\nLa Banque detient ", ANSI_WHITE)
                + printFormat(String.valueOf(bank.getGold()), ANSI_YELLOW)
                + printFormat(" pieces d'or", ANSI_WHITE)
                + printFormat("\nLes Joueurs detiennent ", ANSI_WHITE)
                + printFormat(res.toString(), ANSI_YELLOW)
                + printFormat(" pieces d'or\n", ANSI_WHITE));
        /*System.out.println(printFormat("Tour " + turn, ANSI_BLACK, ANSI_RED_BACKGROUND) +
                " Il reste "
                + printFormat(String.valueOf(pile.numberOfCards()), ANSI_GREEN, ANSI_BOLD)
                + " cartes dans la pioche."
                + "\nLa Banque detient "
                + printFormat(String.valueOf(bank.getGold()), ANSI_YELLOW)
                + " pieces d'or"
                + "\nLes Joueurs detiennent "
                + printFormat(res.toString(), ANSI_YELLOW)
                + " pieces d'or\n");              */
        if (res.get() + bank.getGold() != 30) {
            throw new IllegalStateException("L'or total n'est plus égal à 30");
        }
    }

    /**
     * Show draw or gold decision
     *
     * @param emptyDeck         is the deck empty
     * @param anythingBuildable is there something buildable
     * @param emptyBank         is the bank empty
     * @param isDraw            the drawDecision
     * @param p                 the player
     */
    public void showDrawOrGold(boolean emptyDeck, boolean anythingBuildable, boolean emptyBank, boolean isDraw, Player p) {
        String res = "";
        if (emptyDeck)
            res += printFormat("Le deck ne contient plus de cartes.\n", ANSI_WHITE);
        if (!anythingBuildable)
            res += printName(p) + printFormat("ne peut rien construire dans sa main.\n", ANSI_WHITE);
        if (emptyBank)
            res += printFormat("La banque ne contient plus de pieces d'or.\n", ANSI_WHITE);
        if (isDraw)
            res += printName(p) + printFormat("decide donc de piocher.", ANSI_WHITE);
        else
            res += printName(p)
                    + printFormat("decide donc de prendre ", ANSI_WHITE)
                    + printFormat(String.valueOf(2), ANSI_YELLOW)
                    + printFormat(" pieces d'or.", ANSI_WHITE);
        LOGGER.fine(res);
        //System.out.println(res);
    }

    /**
     * Put the card b in the deck.
     * @param b The Building
     */
    public void putCard(Building b) {
        pile.putCard(b);
    }

    /**
     * Show draw choice.
     *
     * @param builds    the builds
     * @param discarded the discarded
     * @param built     the built
     * @param p         the Player
     */
    public void showDrawChoice(List<Building> builds, List<Building> discarded, List<Building> built, Player p) {
        StringBuilder res = new StringBuilder();
        if (builds.isEmpty()) {
            res.append(printName(p)).append(printFormat("n'a rien pioché", ANSI_WHITE));
        } else {
            res.append(printName(p)).append(printFormat("a pioché : ", ANSI_WHITE)).append(printBuildings(builds, false));
        }
        if (!discarded.isEmpty()) {
            res.append(printFormat("\nIl a choisi de défausser ", ANSI_WHITE)).append(printBuildings(discarded, false));
        }
        if (!built.isEmpty()) {
            res.append(printFormat("\nIl garde ", ANSI_WHITE)).append(printBuildings(built, false));
        }
        LOGGER.fine(String.valueOf(res));
        //System.out.println(res);
    }

    /**
     * Show builds.
     *
     * @param checkBuilding the check building
     * @param toBuild       the to build
     * @param p             the player
     */
    public void showBuilds(List<Building> checkBuilding, List<Building> toBuild, Player p) {
        StringBuilder res = new StringBuilder();
        if (checkBuilding.isEmpty()) {
            res.append(printName(p)).append(printFormat("n'a rien dans sa main : ", ANSI_WHITE));
        } else {
            res.append(printName(p)).append(printFormat("a dans sa main : ", ANSI_WHITE));
            res.append(printBuildings(checkBuilding, false));
        }
        if (toBuild.isEmpty())
            res.append("\n").append(printName(p)).append(printFormat("choisit de ne rien construire", ANSI_WHITE));
        else {
            res.append("\n").append(printName(p)).append(printFormat("choisit de construire ", ANSI_WHITE)).append(printBuildings(toBuild, false));
        }
        LOGGER.fine(String.valueOf(res));
        //System.out.println(res);
    }

    /**
     * Show laboratory effect.
     *
     * @param p    the player
     * @param card the card
     */
    public void showLaboratoryEffect(Player p, Building card) {
        String res = printName(p) + ANSI_WHITE;
        if (isNull(card))
            res += "a recuperé 1 piece d'or ";
        else
            res += "a recuperé 1 piece d'or et a defaussé " + card.getName();
        res += ANSI_RESET;
        LOGGER.fine(res);
        //System.out.println(res);
    }

    /**
     * Show magic school effect.
     *
     * @param p the player
     */
    public void showMagicSchoolEffect(Player p) {
        LOGGER.fine(printName(p) + "recupere une piece de plus des taxes");
        //System.out.println(printName(p) + "recupere une piece de plus des taxes");
    }

    /**
     * Show donjon effect.
     *
     * @param p the player
     */
    public void showDonjonEffect(Player p) {
        LOGGER.fine(printName(p) + "ne peut pas etre detruit par le condottiere");
        //System.out.println(printName(p) + "ne peut pas etre detruit par le condottiere");
    }

    /**
     * Show manufactory effect.
     *
     * @param p     the player
     * @param cards the cards
     */
    public void showManufactoryEffect(Player p, List<Building> cards) {
        StringBuilder res = new StringBuilder();
        res.append(printName(p)).append(printFormat("a defaussé 3 pieces d'or", ANSI_WHITE));
        if (!cards.isEmpty()) {
            res.append(printFormat(" et a pioché ", ANSI_WHITE)).append(printBuildings(cards, false));
        }
        LOGGER.fine(String.valueOf(res));
        //System.out.println(res);
    }

    /**
     * Show taxes.
     *
     * @param d     the district
     * @param p     the player
     * @param taxes the taxes
     */
    public void showTaxes(District d, Player p, int taxes) {
        String res;
        if (taxes <= getBank().getGold())
            res = printName(p)
                    + printFormat("a récupéré " + taxes + " pieces des quartiers ", ANSI_WHITE)
                    + printDistrict(d);
        else
            res = printFormat("La banque n'a plus assez de pieces,", ANSI_WHITE)
                    + printName(p)
                    + printFormat("a récupéré " + getBank().getGold() + " pieces d'or", ANSI_WHITE);
        LOGGER.fine(res);
        //System.out.println(res);
    }

    /**
     * Show prestige effect.
     *
     * @param p        the player
     * @param prestige the prestige
     */
    public void showPrestigeEffect(Player p, Prestige prestige) {
        String res = printName(p)
                + printFormat("a utilisé : ", ANSI_WHITE)
                + prestige.getName()
                + printFormat(".", ANSI_WHITE);
        LOGGER.fine(res);
        //System.out.println(res);
    }

    /**
     * Show character effect.
     *
     * @param p the player
     */
    public void showCharacterEffect(Player p) {
        String res = printName(p)
                + printFormat("a utilisé l'effet de : ", ANSI_WHITE)
                + printRole(p)
                + printFormat(".", ANSI_WHITE);
        LOGGER.fine(res);
        //System.out.println(res);
    }

    /**
     * Show architect effect.
     *
     * @param p     the player
     * @param cards the cards
     */
    public void showArchitectEffect(Player p, List<Building> cards) {
        StringBuilder res = new StringBuilder();
        res.append(printName(p)).append(printFormat("pourra construire 3 batiments ce tour-ci.", ANSI_WHITE));
        if (!cards.isEmpty()) {
            res.append(printName(p)).append(printFormat("a pioché ", ANSI_WHITE)).append(printBuildings(cards, false));
        }
        LOGGER.fine(String.valueOf(res));
        //System.out.println(res);
    }

    /**
     * Show king effect.
     *
     * @param p the player
     */
    public void showKingEffect(Player p) {
        String res = printName(p) + printFormat("commencera au prochain tour", ANSI_WHITE);
        LOGGER.fine(res);
        //System.out.println(res);
    }

    /**
     * Show magician effect.
     *
     * @param p      the player
     * @param target the target
     */
    public void showMagicianEffect(Player p, Player target) {
        StringBuilder res = new StringBuilder();
        if (isNull(target)) {
            res.append(printName(p)).append(printFormat("echange ses cartes avec la pioche. Sa main se compose maintenant de :", ANSI_WHITE));
            res.append(printBuildings(p.getCardHand(), false));
        } else
            res.append(printFormat(" Il echange ses cartes avec", ANSI_WHITE)).append(printName(p));
        LOGGER.fine(String.valueOf(res));
        //System.out.println(res);
    }

    /**
     * Show merchant effect.
     *
     * @param p the player
     */
    public void showMerchantEffect(Player p) {
        String res = printFormat("La banque est vide,", ANSI_WHITE)
                + printName(p)
                + printFormat("n'a rien recupéré.", ANSI_WHITE);
        if (!getBank().isEmpty())
            res = printName(p) + printFormat("a recupere une piece de bonus. ", ANSI_WHITE);
        LOGGER.fine(res);
        //System.out.println(res);
    }

    /**
     * Show condottiere effect.
     *
     * @param target the target
     * @param build  the build
     * @param p      the player
     */
    public void showCondottiereEffect(Player target, Building build, Player p) {
        String res = printRole(p) + printFormat("n'a pas assez d'or ou pas de cible, il n'a donc rien detruit", ANSI_WHITE);
        if (!isNull(target) && !isNull(build))
            res = printFormat("Le batiment ", ANSI_WHITE)
                    + printFormat(build.getName(), ANSI_UNDERLINE, ANSI_YELLOW)
                    + printFormat(" de", ANSI_WHITE)
                    + printName(target)
                    + printFormat("a été ciblé.", ANSI_WHITE);
        LOGGER.fine(res);
        //System.out.println(res);
    }

    /**
     * Show bonus.
     *
     * @param first the first
     * @param p     the player
     */
    public void showBonus(boolean first, Player p) {
        boolean[] vars = p.calculBonus();
        String res = "";
        if (first)
            res += printName(p)
                    + printFormat("a construit 8 bâtiments en premier, il gagne ", ANSI_WHITE)
                    + printFormat(4 + " points bonus", ANSI_GREEN) + "\n";
        else if (vars[2])
            res += printName(p)
                    + printFormat("a construit 8 bâtiments ou plus, il gagne ", ANSI_WHITE)
                    + printFormat(2 + " points bonus", ANSI_CYAN) + "\n";
        if (vars[0])
            res += printName(p)
                    + printFormat("a construit un quartier de chaque District, il gagne ", ANSI_WHITE)
                    + printFormat(3 + " points bonus", ANSI_BLUE) + "\n";
        else if (vars[1])
            res += printName(p)
                    + printFormat("a construit un quartier de quatre Quartiers et la Cour des Miracles, il gagne ", ANSI_WHITE)
                    + printFormat(3 + " points bonus", ANSI_PURPLE) + "\n";
        LOGGER.fine(res);
        //System.out.print(res);
    }

    /**
     * Print buildings string.
     *
     * @param buildings the buildings
     * @param extend    the extent
     * @return String of the list of building
     */
    public static String printBuildings(List<Building> buildings, boolean extend) {
        int wrap = extend ? 3 : 5;
        StringBuilder res = new StringBuilder("\n");
        for (int i = 0; i < buildings.size(); i++) {
            res.append(printBuilding(buildings.get(i), extend)).append("\t");
            if ((i + 1) % wrap == 0)
                res.append("\n");
        }
        return printFormat(res.toString());
    }

    /**
     * Print building string.
     *
     * @param b      the building
     * @param extend the extent
     * @return formatted Building String
     */
    public static String printBuilding(Building b, boolean extend) {
        String res = printFormat(b.getName(), ANSI_ITALIC);
        if (extend) {
            res += printFormat(", Cout : ", ANSI_WHITE) + printFormat(String.valueOf(b.getCost()), ANSI_YELLOW) + printFormat(", Quartier: ", ANSI_WHITE) + printDistrict(b.getDistrict());
        }
        return res;
    }

    /**
     * Print name string.
     *
     * @param p the player
     * @return the name in purple
     */
    public static String printName(Player p) {
        return printFormat(" " + p.getName() + " ", ANSI_PURPLE, ANSI_ITALIC);
    }

    /**
     * Print role string.
     *
     * @param p the player
     * @return the role in Red
     */
    public String printRole(Player p) {
        return printFormat(p.getRole().getName(), ANSI_RED, ANSI_ITALIC);
    }

    /**
     * Print district string.
     *
     * @param d the district
     * @return the district in color
     */
    public static String printDistrict(District d) {
        String c = switch (d) {
            case Noble -> ANSI_YELLOW;
            case Commercial -> ANSI_GREEN;
            case Religion -> ANSI_BLUE;
            case Military -> ANSI_RED;
            case Prestige -> ANSI_PURPLE;
        };
        return printFormat(d.name(), c, ANSI_UNDERLINE);
    }

    /**
     * Print format string.
     *
     * @param text   the message
     * @param format the format
     * @return the string
     */
    public static String printFormat(String text, String... format) {
        StringBuilder res = new StringBuilder();
        for (String f : format)
            res.append(f);
        return res + text + ANSI_RESET;
    }

    /**
     * Print prestige point depending on the building called
     *
     * @param p        the player
     * @param prestige the prestige
     */
    public void printPrestigePoint(Player p, Prestige prestige) {
        LOGGER.fine(printName(p)
                + printFormat(" possede ", ANSI_WHITE)
                + printFormat(prestige.getName(), ANSI_ITALIC, ANSI_PURPLE)
                + printFormat(", il gagne ", ANSI_WHITE)
                + printFormat(2 + " points bonus", ANSI_CYAN) + "\n");
        //System.out.println(printName(p) + " possede " + printFormat(prestige.getName(), ANSI_ITALIC, ANSI_PURPLE) + ", il gagne " + printFormat(2 + " points bonus", ANSI_CYAN) + "\n");
    }

    public void showRole(Player player) {
        LOGGER.fine(printName(player)
                + printFormat(" a choisi le role ", ANSI_WHITE)
                + printRole(player));
        //System.out.println(printName(player) + " a choisi le role " + printRole(player));
    }

    /**
     * write result of the game in results.csv
     */
    public void writeWinner(){
        CsvWrite writer = new CsvWrite();
        writer.write(""); //reset the resources "stats.csv"
        players.sort(PointsOrder);
        int index = 0;
        if (players.get(1).getScore() == players.get(0).getScore()){ //determine if equalities or not
            while (players.get(index).getScore() == players.get(0).getScore() && index < players.size()){
                writer.append(players.get(index).getName() + ",0,1,0");
                index+=1;
            }
            while (index < players.size()){
                writer.append(players.get(index).getName() + ",0,0,1");
                index+=1;
            }
        }
        else {
            writer.append(players.get(0).getName() + ",1,0,0");
            for (index=1;index<players.size(); index++){
                writer.append(players.get(index).getName() + ",0,0,1");
            }
        }
        writer.save(); //save the data
    }
}