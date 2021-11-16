package fr.unice.polytech.startingpoint;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<Player> players;
    private final Deck pile;

    Game(int nb_players){
        pile = new Deck();
        players = new ArrayList<>();
        for(int i=0;i<nb_players;i++){
            players.add(new Player());
        }
    }

    String showBoard(){
        int nb_players = players.size();
        StringBuilder res = new StringBuilder();
        for(int i=0;i<nb_players;i++){
            res.append("Joueur ").append(i + 1).append(" :\n").append(players.get(i)).append("\n");
        }
        return String.valueOf(res);
    }

    void run(){
        for(int i=0;i<5;i++){
            for (Player j: players) {
                j.play(pile);
            }
            System.out.println("Tour 1 :\n" + showBoard() );
        }
    }
}
