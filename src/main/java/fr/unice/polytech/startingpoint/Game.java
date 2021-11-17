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
            players.add(new Player(String.valueOf((i+1))));
        }
    }

    String showBoard(){
        int nb_players = players.size();
        StringBuilder res = new StringBuilder();
        for (Player player : players) {
            res.append("Joueur ").append(player.getName()).append(" :\n").append(player).append("\n");
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

        winners.forEach(e -> System.out.println("Le Joueur "+ e.getName()+ " a gagné avec un score de " +e.getGoldScore() + " Points"));
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
