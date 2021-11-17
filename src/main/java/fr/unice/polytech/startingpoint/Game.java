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

    List<Player> determineWinner(){
        int max =players.get(0).getGoldScore();
        for (Player p : players) {
            if(max<p.getGoldScore()){
                max=p.getGoldScore();
            }
        }
        int finalMax = max;
        return players.stream().filter(e -> e.getGoldScore()== finalMax).toList();
    }

    void showWinner(List<Player> winners ){
        winners.stream().forEach(e -> System.out.println(e.getGoldScore()));
    }

    void run(){
        for(int i=0;i<5;i++){
            for (Player j: players) {
                j.play(pile);
            }
            System.out.println("Tour "+(i+1)+" :\n" + showBoard() );
        }
        showWinner(determineWinner());
    }
}
