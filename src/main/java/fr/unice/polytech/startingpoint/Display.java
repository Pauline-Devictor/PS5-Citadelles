package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.buildings.Prestige;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static fr.unice.polytech.startingpoint.strategies.Player.PointsOrder;
import static java.util.Objects.isNull;

public class Display {

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
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

    /**
     * Print the turn of a player
     *
     * @param p        the player
     * @param goldDraw the gold at the beginning of the turn
     */
    public static void showPlay(Player p, int goldDraw) {
        String res = printFormat("---------------------------------------------------------------", ANSI_WHITE, ANSI_BLACK_BACKGROUND);
        if (!isNull(p.getRole()) && p.getRole().isMurdered())
            res += "\n" + printName(p) + printFormat("a ete tu??. Son tour est pass??\n", ANSI_WHITE);
        else {
            int showGold = (p.getGold() - goldDraw);
            String signe = ANSI_RED + "";
            if (showGold > 0)
                signe = ANSI_GREEN + "+";
            res += "\n" + printName(p) + "(" + printRole(p) + ")" +
                    printFormat(" poss??de ", ANSI_WHITE)
                    + printFormat(String.valueOf(p.getGold()), ANSI_YELLOW, ANSI_BOLD)
                    + "(" + signe + showGold + ANSI_RESET
                    + printFormat(") pieces d'or" + ", ", ANSI_WHITE)
                    + printFormat(String.valueOf(p.getCardHand().size()), ANSI_CYAN, ANSI_BOLD)
                    + printFormat(" cartes et ", ANSI_WHITE)
                    + printFormat(String.valueOf(p.getCity().size()), ANSI_BOLD, ANSI_BLUE)
                    + printFormat(" b??timents\n", ANSI_WHITE);
        }
        res += printFormat("---------------------------------------------------------------", ANSI_WHITE, ANSI_BLACK_BACKGROUND);
        LOGGER.fine(res);
    }

    /**
     * Print the ranking of the players
     */
    static void showRanking(List<Player> players) {
        players.sort(PointsOrder);
        for (int i = 1; i <= players.size(); i++) {
            LOGGER.config(printFormat(i + ". Avec ", ANSI_WHITE)
                    + printFormat(String.valueOf(players.get(i - 1).getScore()), ANSI_BOLD, ANSI_GREEN)
                    + printFormat(" points, c'est ", ANSI_WHITE)
                    + printName(players.get(i - 1))
            );
        }
    }

    /**
     * Show stats when 1000 games are thrown
     */
    static void showStats(Map<String, int[]> results) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumFractionDigits(3);
        StringBuilder res = new StringBuilder();
        results.forEach((k, v) -> {
            int total = v[3];
            int win = v[1];
            int draw = v[2];
            int lose = total - win - draw;
            int avg = v[0] / total;
            res.append(k).append("\n").append("\tVictoires : ").append(win).append(String.format(" (%.02f", (double) win / (double) total * 100)).append("%)\n");
            res.append("\tD??faites : ").append(lose).append(String.format(" (%.02f", (double) lose / (double) total * 100)).append("%)\n");
            res.append("\tNuls : ").append(draw).append(String.format(" (%.02f", (double) draw / (double) total * 100)).append("%)\n");
            res.append("\tScore Moyen : ").append(avg).append("\n");
        });
        LOGGER.config(String.valueOf(res));
    }

    /**
     * Show board.
     */
    static void showBoard(List<Player> players) {
        StringBuilder res = new StringBuilder();
        players.forEach(e -> res.append(e).append("\n"));
        LOGGER.fine(String.valueOf(res));
    }

    /**
     * Show infos of the turn,where the golds are and the number of cards in the deck
     *
     * @param turn the turn
     */
    static void showVariables(int turn, List<Player> players, Bank bank, Deck pile) {
        AtomicInteger res = new AtomicInteger();
        players.forEach(e -> res.addAndGet(e.getGold()));
        LOGGER.fine(printFormat("Tour " + turn, ANSI_BLACK, ANSI_RED_BACKGROUND)
                + printFormat(" Il reste ", ANSI_WHITE)
                + printFormat(String.valueOf(pile.numberOfCards()), ANSI_GREEN, ANSI_BOLD)
                + printFormat(" cartes dans la pioche.", ANSI_WHITE)
                + printFormat("\nLa Banque d??tient ", ANSI_WHITE)
                + printFormat(String.valueOf(bank.getGold()), ANSI_YELLOW)
                + printFormat(" pieces d'or", ANSI_WHITE)
                + printFormat("\nLes Joueurs d??tiennent ", ANSI_WHITE)
                + printFormat(res.toString(), ANSI_YELLOW)
                + printFormat(" pieces d'or\n", ANSI_WHITE));
        if (res.get() + bank.getGold() != 30) {
            throw new IllegalStateException("L'or total n'est plus ??gal ?? 30");
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
    public static void showDrawOrGold(boolean emptyDeck, boolean anythingBuildable, boolean emptyBank, boolean isDraw, Player p) {
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
     * Show draw choice.
     *
     * @param builds    the builds
     * @param discarded the discarded
     * @param built     the built
     * @param p         the Player
     */
    public static void showDrawChoice(List<Building> builds, List<Building> discarded, List<Building> built, Player p) {
        StringBuilder res = new StringBuilder();
        if (builds.isEmpty()) {
            res.append(printName(p)).append(printFormat("n'a rien pioch??", ANSI_WHITE));
        } else {
            res.append(printName(p)).append(printFormat("a pioch?? : ", ANSI_WHITE)).append(printBuildings(builds, false));
        }
        if (!discarded.isEmpty()) {
            res.append(printFormat("\nIl a choisi de d??fausser ", ANSI_WHITE)).append(printBuildings(discarded, false));
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
    public static void showBuilds(List<Building> checkBuilding, List<Building> toBuild, Player p) {
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
    public static void showLaboratoryEffect(Player p, Building card) {
        String res = printName(p) + ANSI_WHITE;
        if (isNull(card))
            res += "a r??cup??r?? 1 piece d'or ";
        else
            res += "a r??cup??r?? 1 piece d'or et a d??fauss?? " + card.getName();
        res += ANSI_RESET;
        LOGGER.fine(res);
        //System.out.println(res);
    }

    /**
     * Show magic school effect.
     *
     * @param p the player
     */
    public static void showMagicSchoolEffect(Player p) {
        LOGGER.fine(printName(p) + "r??cup??r?? une piece de plus des taxes");
    }

    /**
     * Show donjon effect.
     *
     * @param p the player
     */
    public static void showDonjonEffect(Player p) {
        LOGGER.fine(printName(p) + "ne peut pas ??tre d??truit par le condottiere");
    }

    /**
     * Show manufacture effect.
     *
     * @param p     the player
     * @param cards the cards
     */
    public static void showManufactureEffect(Player p, List<Building> cards) {
        StringBuilder res = new StringBuilder();
        res.append(printName(p)).append(printFormat("a d??fauss?? 3 pieces d'or", ANSI_WHITE));
        if (!cards.isEmpty()) {
            res.append(printFormat(" et a pioch?? ", ANSI_WHITE)).append(printBuildings(cards, false));
        }
        LOGGER.fine(String.valueOf(res));
    }

    /**
     * Show taxes.
     *
     * @param d     the district
     * @param p     the player
     * @param taxes the taxes
     */
    public static void showTaxes(District d, Player p, int taxes, Bank bank) {
        String res;
        if (taxes <= bank.getGold())
            res = printName(p)
                    + printFormat("a r??cup??r?? " + taxes + " pieces des quartiers ", ANSI_WHITE)
                    + printDistrict(d);
        else
            res = printFormat("La banque n'a plus assez de pieces,", ANSI_WHITE)
                    + printName(p)
                    + printFormat("a r??cup??r?? " + bank.getGold() + " pieces d'or", ANSI_WHITE);
        LOGGER.fine(res);
    }

    /**
     * Show prestige effect.
     *
     * @param p        the player
     * @param prestige the prestige
     */
    public static void showPrestigeEffect(Player p, Prestige prestige) {
        String res = printName(p)
                + printFormat("a utilis?? : ", ANSI_WHITE)
                + prestige.getName()
                + printFormat(".", ANSI_WHITE);
        LOGGER.fine(res);
    }

    /**
     * Show character effect.
     *
     * @param p the player
     */
    public static void showCharacterEffect(Player p) {
        String res = printName(p)
                + printFormat("a utilis?? l'effet de : ", ANSI_WHITE)
                + printRole(p)
                + printFormat(".", ANSI_WHITE);
        LOGGER.fine(res);
    }

    /**
     * Show architect effect.
     *
     * @param p     the player
     * @param cards the cards
     */
    public static void showArchitectEffect(Player p, List<Building> cards) {
        StringBuilder res = new StringBuilder();
        res.append(printName(p)).append(printFormat("pourra construire 3 b??timents ce tour-ci.", ANSI_WHITE));
        if (!cards.isEmpty()) {
            res.append(printName(p)).append(printFormat("a pioch?? ", ANSI_WHITE)).append(printBuildings(cards, false));
        }
        LOGGER.fine(String.valueOf(res));
    }

    /**
     * Show king effect.
     *
     * @param p the player
     */
    public static void showKingEffect(Player p) {
        String res = printName(p) + printFormat("commencera au prochain tour", ANSI_WHITE);
        LOGGER.fine(res);
    }

    /**
     * Show magician effect.
     *
     * @param p      the player
     * @param target the target
     */
    public static void showMagicianEffect(Player p, Player target) {
        StringBuilder res = new StringBuilder();
        if (isNull(target)) {
            res.append(printName(p)).append(printFormat("??change ses cartes avec la pioche. Sa main se compose maintenant de :", ANSI_WHITE));
            res.append(printBuildings(p.getCardHand(), false));
        } else
            res.append(printFormat(" Il ??change ses cartes avec", ANSI_WHITE)).append(printName(p));
        LOGGER.fine(String.valueOf(res));
    }

    /**
     * Show merchant effect.
     *
     * @param p the player
     */
    public static void showMerchantEffect(Player p) {
        Bank b = p.getBoard().getBank();
        String res = printFormat("La banque est vide,", ANSI_WHITE)
                + printName(p)
                + printFormat("n'a rien r??cup??r??.", ANSI_WHITE);
        if (!b.isEmpty())
            res = printName(p) + printFormat("a r??cup??r?? une piece de bonus. ", ANSI_WHITE);
        LOGGER.fine(res);
    }

    /**
     * Show condottiere effect.
     *
     * @param target the target
     * @param build  the build
     * @param p      the player
     */
    public static void showCondottiereEffect(Player target, Building build, Player p) {
        String res = printRole(p) + printFormat("n'a pas assez d'or ou pas de cible, il n'a donc rien d??truit", ANSI_WHITE);
        if (!isNull(target) && !isNull(build))
            res = printFormat("Le b??timent ", ANSI_WHITE)
                    + printFormat(build.getName(), ANSI_UNDERLINE, ANSI_YELLOW)
                    + printFormat(" de", ANSI_WHITE)
                    + printName(target)
                    + printFormat("a ??t?? cibl??.", ANSI_WHITE);
        LOGGER.fine(res);
    }

    /**
     * Show bonus.
     *
     * @param first the first
     * @param p     the player
     */
    public static void showBonus(boolean first, Player p) {
        boolean[] vars = p.calculBonus();
        String res = "";
        if (first)
            res += printName(p)
                    + printFormat("a construit 8 b??timents en premier, il gagne ", ANSI_WHITE)
                    + printFormat(4 + " points bonus", ANSI_GREEN) + "\n";
        else if (vars[2])
            res += printName(p)
                    + printFormat("a construit 8 b??timents ou plus, il gagne ", ANSI_WHITE)
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
    public static String printRole(Player p) {
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
    public static void printPrestigePoint(Player p, Prestige prestige) {
        LOGGER.fine(printName(p)
                + printFormat(" poss??de ", ANSI_WHITE)
                + printFormat(prestige.getName(), ANSI_ITALIC, ANSI_PURPLE)
                + printFormat(", il gagne ", ANSI_WHITE)
                + printFormat(2 + " points bonus", ANSI_CYAN) + "\n");
    }

    public static void showRole(Player player) {
        LOGGER.fine(printName(player)
                + printFormat(" a choisi le role ", ANSI_WHITE)
                + printRole(player));
    }
}
