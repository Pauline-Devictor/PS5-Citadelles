package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.buildings.Prestige;
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
    }

    public void showPlay(Player p, int goldDraw) {
        if (!isNull(p.getRole()) && p.getRole().isMurdered())
            System.out.println(ANSI_ITALIC + p.getName() + " has been killed. Turn is skipped.\n" + ANSI_RESET);
        else {
            int showGold = (p.getGold() - goldDraw);
            String signe = ANSI_RED + "";
            if (showGold > 0)
                signe = ANSI_GREEN + "+";
            String res = ANSI_CYAN + p.getName() + ANSI_RESET + " (" + ANSI_ITALIC + p.getRole().getName() + ANSI_RESET + ") " +
                    " possède " + ANSI_YELLOW + p.getGold() + ANSI_RESET + "(" + signe + showGold + ANSI_RESET + ") pieces d'or" +
                    "\nIl possede " + p.getCardHand().size() + " cartes et " + p.getCity().size() + " batiments";
            System.out.println(res + "\n");
        }
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
        if (res.get() + bank.getGold() != 30) {
            throw new IllegalStateException("L'or total n'est plus égal à 30");
        }
    }

    public void showDrawOrGold(boolean emptyDeck, boolean anythingBuildable, boolean emptyBank, boolean isDraw, String name) {
        String res = "";
        if (emptyDeck)
            res += "Le deck ne contient plus de cartes.\n";
        if (!anythingBuildable)
            res += name + " ne peut rien construire dans sa main.\n";
        if (emptyBank)
            res += "La banque ne contient plus de pieces d'or.\n";
        if (isDraw)
            res += name + " decide donc de piocher.";
        else
            res += name + " decide donc de prendre 2 pieces d'or.";
        System.out.println(res);
    }

    public void putCard(Building b) {
        pile.putCard(b);
    }

    public void showDrawChoice(List<Building> builds, List<Building> discarded, List<Building> builded, String name) {
        StringBuilder res = new StringBuilder();
        if (builds.isEmpty()) {
            res.append(name).append(" n'a rien pioché");
        } else {
            res.append(name).append(" a pioché : ");
            for (Building b : builds)
                res.append(b.getName()).append(", ");
        }
        if (!discarded.isEmpty()) {
            res.append("\nIl a choisi de défausser ");
            for (Building b : discarded)
                res.append(b.getName()).append(", ");
        }
        if (!builded.isEmpty()) {
            res.append("\nIl garde ");
            for (Building b : builded)
                res.append(b.getName()).append(", ");
        }
        System.out.println(res);
    }

    public void showBuilds(List<Building> checkBuilding, List<Building> toBuild, String name) {
        StringBuilder res = new StringBuilder();
        if (checkBuilding.isEmpty()) {
            res.append(name).append(" n'a rien dans sa main : ");
        } else {
            res.append(name).append(" a dans sa main : ");
            for (Building b : checkBuilding)
                res.append(b.getName()).append(", ");
        }
        if (toBuild.isEmpty())
            res.append("\n").append(name).append(" choisit de ne rien construire");
        else {
            res.append("\n").append(name).append(" choisit de construire ");
            for (Building b : toBuild)
                res.append(b.getName()).append(", ");
        }
        System.out.println(res);
    }

    public void showLaboratoryEffect(Player p, String cardName) {
        String res = "";
        if (cardName.equals(""))
            res += " Il a recuperé 1 piece d'or ";
        else
            res += " Il a recuperé 1 piece d'or et a defaussé " + cardName;
        System.out.println(res);
    }

    public void showMagicSchoolEffect(Player p) {
        System.out.println(p.getName() + " recupere une piece de plus des taxes");
    }

    public void showManufactoryEffect(Player p, List<Building> cards) {
        StringBuilder res = new StringBuilder();
        res.append(p.getName()).append(" a defaussé 3 pieces d'or");
        if (!cards.isEmpty()) {
            res.append(" et a pioché ");
            for (Building b : cards) {
                res.append(b.getName()).append(", ");
            }
        }
        System.out.println(res);
    }

    public void showTaxes(District d, Player p, int taxes) {
        String res;
        if (taxes <= getBank().getGold())
            res = p.getName() + " a récupéré " + taxes + " pieces des quartiers " + d.name();
        else
            res = "La banque n'a plus assez de pieces, " + p.getName() + " a récupéré " + getBank().getGold() + " pieces d'or";
        System.out.println(res);
    }

    public void showPrestigeEffect(Player p, Prestige prestige) {
        String res = p.getName() + " a utilisé : " + prestige.getName() + ".";
        System.out.println(res);
    }

    public void showCharacterEffect(Player p, Character c) {
        String res = p.getName() + " a utilisé l'effet de : " + c.getName() + ".";
        System.out.println(res);
    }

    public void showArchitectEffect(Player p, List<Building> cards) {
        StringBuilder res = new StringBuilder();
        res.append(p.getName()).append(" pourra construire 3 batiments ce tour-ci.");
        if (!cards.isEmpty()) {
            res.append(p.getName()).append(" a pioché ");
            for (Building b : cards) {
                res.append(b.getName()).append(", ");
            }
        }
        System.out.println(res);
    }


    public void showKingEffect(Player p) {
        String res = p.getName() + " commencera au prochain tour";
        System.out.println(res);
    }

    public void showMagicianEffect(Player p, Player target) {
        StringBuilder res = new StringBuilder();
        if (isNull(target)) {
            res.append(" Il echange ses cartes avec la pioche. Sa main se compose maintenant de ");
            for (Building b : p.getCardHand()) {
                res.append(b.getName()).append(", ");
            }
        } else
            res.append(" Il echange ses cartes avec ").append(target.getName());
        System.out.println(res);
    }

    public void showMerchantEffect(Player p) {
        String res = "La banque est vide, " + p.getName() + " n'a rien recupéré.";
        if (!getBank().isEmpty())
            res = p.getName() + " a recupere une piece de bonus. ";
        System.out.println(res);
    }

    public void showCondottiereEffect(Player target, Building build) {
        if (!isNull(target))
            System.out.println("Le batiment " + build.getName() + " du joueur " + target.getName() + " a été ciblé.");
        else
            System.out.println("Le condottiere n'a pas assez d'or, il n'a donc rien detruit");
    }
}

