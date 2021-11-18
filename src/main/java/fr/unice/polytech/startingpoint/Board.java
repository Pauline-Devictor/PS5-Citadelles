package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.Characters.*;
import fr.unice.polytech.startingpoint.Characters.Character;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Deck pile;
    private final Bank bank;
    private final List<Character> characters = new ArrayList<>();

    public Deck getPile() {
        return pile;
    }

    public Bank getBank() {
        return bank;
    }

    Board(){
        this.bank = new Bank(30);
        this.pile = new Deck();
        this.characters.add(new Assassin());
        this.characters.add(new Thief());
        this.characters.add(new Magician());
        this.characters.add(new King());
        this.characters.add(new Bishop());
        this.characters.add(new Merchant());
        this.characters.add(new Architect());
        this.characters.add(new Condottiere());
    }

    String showBoard(List<Player> players){
        int nb_players = players.size();
        StringBuilder res = new StringBuilder();
        for (Player player : players) {
            res.append("Joueur ").append(player.getName()).append(" :\n").append(player).append("\n");
        }
        return String.valueOf(res);
    }
}
