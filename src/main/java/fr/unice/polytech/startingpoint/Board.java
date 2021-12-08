package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.strategies.*;

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

    Board() {
        this(4);
    }

    public List<Player> generatePlayers(int nbPlayers) {
        List<Player> players = new ArrayList<>();
        players.add(new RushMerch(this));
        players.add(new RushArchi(this));
        players.add(new HighScoreThief(this));
        players.add(new HighScoreArchi(this));
        return players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    //Libère tous les roles et vide la liste des roles pris
    void setAllFree() {
        characters.forEach(Character::resetRole);
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
    }

    public void showPlay(Player p, int goldDraw, int goldCollect, List<Building> checkDraw, List<Building> checkBuilding) {
        int showGold = (p.getGold() - goldDraw);
        String signe = ANSI_RED + "";
        if (showGold > 0)
            signe = ANSI_GREEN + "+";
        StringBuilder res = new StringBuilder(ANSI_CYAN + p.getName() + ANSI_RESET);
        if (p.getRole().isPresent())
            res.append(" (" + ANSI_ITALIC).append(p.getRole().get().getName()).append(ANSI_RESET).append(") ");
        res.append(" possède " + ANSI_YELLOW).append(p.getGold()).append(ANSI_RESET).append("(").append(signe).append(showGold).append(ANSI_RESET).append(") pieces d'or");
        if (checkDraw.size() > 0) {
            res.append(", a pioché " + ANSI_UNDERLINE);
            checkDraw.forEach(e -> res.append(e.getName()).append(", "));
            res.append(ANSI_RESET);
        }

        if (checkBuilding.size() > 0) {
            res.append(" et a construit ");
            for (Building e : checkBuilding) {
                res.append(ANSI_BOLD).append(e.getName()).append(ANSI_RESET).append(", ");
            }
        }
        if (goldCollect > 0)
            res.append(" et a récupéré ").append(goldCollect).append(" pieces des impôts");
        System.out.println(res + "\n");
    }

    void showRanking() {
        players.sort(PointsOrder);
        for (int i = 1; i <= players.size(); i++) {
            System.out.println(i + ". Avec " + players.get(i - 1).getGoldScore() + " points, c'est " + players.get(i - 1).getName());
        }
    }

    void showBoard() {
        StringBuilder res = new StringBuilder();
        for (Player player : players) {
            res.append("Joueur ").append(player.getName()).append(" :\n").append(player).append("\n");
        }
        System.out.println(res);
    }

    void showVariables(int turn) {
        AtomicInteger res = new AtomicInteger();
        players.forEach(e -> res.addAndGet(e.getGold()));
        System.out.println(ANSI_RED_BACKGROUND + ANSI_BLACK + "Tour " + (turn) + ":" + ANSI_RESET +
                " Cartes Restantes : " + pile.numberOfCards() + " Or Dans la Banque : " + bank.getGold() + " Or Joueurs :" + res);
    }

}
