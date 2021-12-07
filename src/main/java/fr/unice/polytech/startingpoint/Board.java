package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.ArrayList;
import java.util.List;

import static fr.unice.polytech.startingpoint.Main.*;
import static fr.unice.polytech.startingpoint.Main.ANSI_RESET;
import static java.util.Objects.isNull;

public class Board {
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
        players = new ArrayList<>();
        for (int i = 1; i <= nbPlayers; i++) {
            players.add(new Player(this, String.valueOf(i)));
        }
    }

    Board() {
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
        players = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            players.add(new Player(this, String.valueOf(i)));
        }
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

    public void showPlay(Player p, int goldDraw, int goldCollect, Building checkDraw, List<Building> checkBuilding) {
        int showGold = (p.getGold() - goldDraw);
        String signe = ANSI_RED + "";
        if (showGold > 0)
            signe = ANSI_GREEN + "+";
        StringBuilder res = new StringBuilder(ANSI_CYAN + p.getName() + ANSI_RESET + " (" + ANSI_ITALIC + p.getRole() + ANSI_RESET + ") possède " + ANSI_YELLOW + p.getGold() + ANSI_RESET
                + "(" + signe + showGold + ANSI_RESET + ") pieces d'or");
        if (!isNull(checkDraw))
            res.append(", a pioché " + ANSI_UNDERLINE).append(checkDraw.getName()).append(ANSI_RESET);
        if (checkBuilding.size() > 0) {
            res.append(" et a construit ");
            for (Building e : checkBuilding) {
                res.append(ANSI_BOLD).append(e.getName()).append(ANSI_RESET).append(", ");
            }
        }
        if (goldCollect > 0)
            res.append(" et a récupéré ").append(goldCollect).append(" pieces des impôts");
        System.out.println(res);
    }

    /*String showBoard(List<Player> players){
        StringBuilder res = new StringBuilder();
        for (Player player : players) {
            res.append("Joueur ").append(player.getName()).append(" :\n").append(player).append("\n");
        }
        return String.valueOf(res);
    }*/

}
