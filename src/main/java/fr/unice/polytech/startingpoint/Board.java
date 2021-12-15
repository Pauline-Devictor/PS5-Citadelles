package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.buildings.Prestige;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.strategies.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static fr.unice.polytech.startingpoint.strategies.Player.PointsOrder;
import static java.util.Objects.isNull;

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

    public Board() {
        this(4);
    }

    public List<Player> generatePlayers(int nbPlayers) {
        List<Player> players = new ArrayList<>();
        players.add(new RushMerch(this));
        players.add(new RushArchi(this));
        players.add(new HighScoreArchi(this));
        players.add(new HighScoreThief(this));
        for (int i = 4; i < nbPlayers; i++)
            players.add(new Player(this));
        return players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    //Libère tous les roles et vide la liste des roles pris
    void release() {
        characters.forEach(Character::resetRole);
        players.forEach(Player::reset);
    }

    public Deck getPile() {
        return pile;
    }

    public Bank getBank() {
        return bank;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public Character getCharactersInfos(int index) {
        return characters.get(index);
    } //TODO Find a better way

    public void showPlay(Player p, int goldDraw) {
        String res = printFormat("---------------------------------------------------------------", ANSI_WHITE, ANSI_BLACK_BACKGROUND);
        if (!isNull(p.getRole()) && p.getRole().isMurdered())
            res += "\n" + printName(p) + "a ete tué. Son tour est passé\n";
        else {
            int showGold = (p.getGold() - goldDraw);
            String signe = ANSI_RED + "";
            if (showGold > 0)
                signe = ANSI_GREEN + "+";
            res += "\n" + printName(p) + "(" + printRole(p) + ")" +
                    " possède " + printFormat(String.valueOf(p.getGold()), ANSI_YELLOW, ANSI_BOLD) + "(" + signe + showGold + ANSI_RESET + ") pieces d'or"
                    + ", " + printFormat(String.valueOf(p.getCardHand().size()), ANSI_CYAN, ANSI_BOLD)
                    + " cartes et " + printFormat(String.valueOf(p.getCity().size()), ANSI_BOLD, ANSI_BLUE) + " batiments\n";
            res += printFormat("---------------------------------------------------------------", ANSI_WHITE, ANSI_BLACK_BACKGROUND);
        }
        System.out.println(res + "\n\n");
    }

    void showRanking() {
        players.sort(PointsOrder);
        for (int i = 1; i <= players.size(); i++) {
            System.out.println(i + ". Avec " + printFormat(String.valueOf(players.get(i - 1).getGoldScore()), ANSI_BOLD, ANSI_GREEN) + " points, c'est " + printName(players.get(i - 1)));
        }
    }

    void showBoard() {
        StringBuilder res = new StringBuilder();
        players.forEach(e -> res.append(e).append("\n"));
        System.out.println(res);
    }

    void showVariables(int turn) {
        AtomicInteger res = new AtomicInteger();
        players.forEach(e -> res.addAndGet(e.getGold()));
        System.out.println(printFormat("Tour " + turn, ANSI_BLACK, ANSI_RED_BACKGROUND) +
                " Il reste " + printFormat(String.valueOf(pile.numberOfCards()), ANSI_GREEN, ANSI_BOLD) + " cartes dans la pioche."
                + "\nLa Banque detient " + printFormat(String.valueOf(bank.getGold()), ANSI_YELLOW) + " pieces d'or"
                + "\nLes Joueurs detiennent " + printFormat(res.toString(), ANSI_YELLOW) + " pieces d'or\n");
        if (res.get() + bank.getGold() != 30) {
            throw new IllegalStateException("L'or total n'est plus égal à 30");
        }
    }

    public void showDrawOrGold(boolean emptyDeck, boolean anythingBuildable, boolean emptyBank, boolean isDraw, Player p) {
        String res = "";
        if (emptyDeck)
            res += "Le deck ne contient plus de cartes.\n";
        if (!anythingBuildable)
            res += printName(p) + "ne peut rien construire dans sa main.\n";
        if (emptyBank)
            res += "La banque ne contient plus de pieces d'or.\n";
        if (isDraw)
            res += printName(p) + "decide donc de piocher.";
        else
            res += printName(p) + "decide donc de prendre " + printFormat(String.valueOf(2), ANSI_YELLOW) + " pieces d'or.";
        System.out.println(res);
    }

    public void putCard(Building b) {
        pile.putCard(b);
    }

    public void showDrawChoice(List<Building> builds, List<Building> discarded, List<Building> builded, Player p) {
        StringBuilder res = new StringBuilder();
        if (builds.isEmpty()) {
            res.append(printName(p)).append("n'a rien pioché");
        } else {
            res.append(printName(p)).append("a pioché : ").append(printBuildings(builds));
        }
        if (!discarded.isEmpty()) {
            res.append("\nIl a choisi de défausser ").append(printBuildings(discarded));
        }
        if (!builded.isEmpty()) {
            res.append("\nIl garde ").append(printBuildings(builded));
        }
        System.out.println(res);
    }

    public void showBuilds(List<Building> checkBuilding, List<Building> toBuild, Player p) {
        StringBuilder res = new StringBuilder();
        if (checkBuilding.isEmpty()) {
            res.append(printName(p)).append("n'a rien dans sa main : ");
        } else {
            res.append(printName(p)).append("a dans sa main : ");
            res.append(printBuildings(checkBuilding));
        }
        if (toBuild.isEmpty())
            res.append("\n").append(printName(p)).append("choisit de ne rien construire");
        else {
            res.append("\n").append(printName(p)).append("choisit de construire ").append(printBuildings(toBuild));
        }
        System.out.println(res);
    }

    public void showLaboratoryEffect(Player p, Building cardName) {
        String res = printName(p);
        if (isNull(cardName))
            res += "a recuperé 1 piece d'or ";
        else
            res += "a recuperé 1 piece d'or et a defaussé " + cardName.getName();
        System.out.println(res);
    }

    public void showMagicSchoolEffect(Player p) {
        System.out.println(printName(p) + "recupere une piece de plus des taxes");
    }

    public void showManufactoryEffect(Player p, List<Building> cards) {
        StringBuilder res = new StringBuilder();
        res.append(printName(p)).append("a defaussé 3 pieces d'or");
        if (!cards.isEmpty()) {
            res.append("et a pioché ").append(printBuildings(cards));
        }
        System.out.println(res);
    }

    public void showTaxes(District d, Player p, int taxes) {
        String res;
        if (taxes <= getBank().getGold())
            res = printName(p) + "a récupéré " + taxes + " pieces des quartiers " + printDistrict(d);
        else
            res = "La banque n'a plus assez de pieces," + printName(p) + "a récupéré " + getBank().getGold() + " pieces d'or";
        System.out.println(res);
    }

    public void showPrestigeEffect(Player p, Prestige prestige) {
        String res = printName(p) + "a utilisé : " + prestige.getName() + ".";
        System.out.println(res);
    }

    public void showCharacterEffect(Player p) {
        String res = printName(p) + "a utilisé l'effet de : " + printRole(p) + ".";
        System.out.println(res);
    }

    public void showArchitectEffect(Player p, List<Building> cards) {
        StringBuilder res = new StringBuilder();
        res.append(printName(p)).append("pourra construire 3 batiments ce tour-ci.");
        if (!cards.isEmpty()) {
            res.append(printName(p)).append("a pioché ").append(printBuildings(cards));
        }
        System.out.println(res);
    }


    public void showKingEffect(Player p) {
        String res = printName(p) + "commencera au prochain tour";
        System.out.println(res);
    }

    public void showMagicianEffect(Player p, Player target) {
        StringBuilder res = new StringBuilder();
        if (isNull(target)) {
            res.append(printName(p)).append("echange ses cartes avec la pioche. Sa main se compose maintenant de :");
            res.append(printBuildings(p.getCardHand()));
        } else
            res.append(" Il echange ses cartes avec").append(printName(p));
        System.out.println(res);
    }

    public void showMerchantEffect(Player p) {
        String res = "La banque est vide," + printName(p) + "n'a rien recupéré.";
        if (!getBank().isEmpty())
            res = printName(p) + "a recupere une piece de bonus. ";
        System.out.println(res);
    }

    public void showCondottiereEffect(Player target, Building build, Player p) {
        String res = printRole(p) + "n'a pas assez d'or ou pas de cible, il n'a donc rien detruit";
        if (!isNull(target))
            res = ("Le batiment " + printFormat(build.getName(), ANSI_UNDERLINE, ANSI_YELLOW) + " de" + printName(target) + "a été ciblé.");
        System.out.println(res);
    }

    public String printBuildings(List<Building> buildings) {
        StringBuilder res = new StringBuilder("\n");
        for (int i = 0; i < buildings.size(); i++) {
            res.append(printBuilding(buildings.get(i))).append(", ");
            if ((i + 1) % 5 == 0)
                res.append("\n");
        }
        //TODO Retirer derniere virgule
        return printFormat(res.toString());
    }

    public String printBuilding(Building b) {
        return printFormat(b.getName(), ANSI_ITALIC, ANSI_UNDERLINE);
    }

    public String printName(Player p) {
        return printFormat(" " + p.getName() + " ", ANSI_PURPLE, ANSI_ITALIC);
    }

    public String printRole(Player p) {
        return printFormat(p.getRole().getName(), ANSI_RED, ANSI_ITALIC);
    }

    public String printDistrict(District d) {
        String c = switch (d) {
            case Noble -> ANSI_YELLOW;
            case Commercial -> ANSI_GREEN;
            case Religion -> ANSI_BLUE;
            case Military -> ANSI_RED;
            case Prestige -> ANSI_PURPLE;
        };
        return printFormat(d.name(), c, ANSI_UNDERLINE);
    }

    public static String printFormat(String text, String... format) {
        StringBuilder res = new StringBuilder();
        for (String f : format)
            res.append(f);
        return res + text + ANSI_RESET;
    }
}