package fr.unice.polytech.startingpoint;

import java.util.List;

public class Board {
    private final Deck pile;
    private final Bank bank;

    public Deck getPile() {
        return pile;
    }

    public Bank getBank() {
        return bank;
    }

    Board(){
        this.bank = new Bank(30);
        this.pile = new Deck();
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
