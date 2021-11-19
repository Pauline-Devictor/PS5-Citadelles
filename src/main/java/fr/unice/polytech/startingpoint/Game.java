package fr.unice.polytech.startingpoint;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Board board;
    private final List<Player> players;

    Game(Board b, int nb_players){
        board = b;
        players = new ArrayList<>();
        for(int i=0;i<nb_players;i++){
            players.add(new Player(board, String.valueOf(i+1)));
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    List<Player> determineWinner(){
        List<Player> list_players =getPlayers();
        if(list_players.size() > 0){
            int max = list_players.get(0).getGoldScore();
            for (Player p : list_players) {
                if(max<p.getGoldScore()){
                    max=p.getGoldScore();
                }
            }
            int finalMax = max;
            list_players = list_players.stream().filter(e -> e.getGoldScore()== finalMax).toList();
        }

        return list_players;
    }

    void showWinner(List<Player> winners ){
        winners.forEach(e -> System.out.println("Le Joueur "+ e.getName()+ " a gagné avec un score de " +e.getGoldScore() + " Points"));
    }

    void run(){
        for(int i=0;i<5;i++){
            for (Player j: players) {
                j.play();
            }
            System.out.println("Tour " + (i+1) + " :\n" + board.showBoard(players) );
        }
        showWinner(determineWinner());
    }
}
