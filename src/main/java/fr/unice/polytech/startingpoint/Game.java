package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.Character;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Board board;
    private final List<Player> players;
    private final int  nb_players;

    //TODO : Creer un board a l'init ???
    Game(Board b, int nb_players){
        this.nb_players = nb_players;
        board = b;
        players = new ArrayList<>();
        for(int i=0;i<nb_players;i++){
            players.add(new Player(board, String.valueOf(i + 1)));
        }
        players.get(0).takeCrown();
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

    List<Player> newGame(){
        boolean endOfGame = false;
        int turn=1;
        while (!endOfGame){
            List<Player> roleOrder = getOrderPlayer();

            for (Player p:roleOrder) {
                p.chooseRole();
                p.getRole().setPlayer(p);
            }
            for(Character c: board.getCharacters()){
                if (c.getPlayer() != null){
                    c.getPlayer().play();
                    //crown -> selection des roles (le dernier roi la recupere)
                    if (c.getPlayer().getRole().getName().equals("King")){
                        clearCrown();
                        c.getPlayer().takeCrown();}
                }
            }

            System.out.println("\nTour " + (turn) + " :\n" + board.showBoard(players) );
            board.setAllFree();
            turn++;

            for(Player p : players){
                if(p.getBuildings().stream().filter(Building::getBuilt).count()>=8)
                    endOfGame=true;
            }
        }
        return determineWinner();
    }

    List<Player> getOrderPlayer() {
        int index = 0;
        List<Player> alternateList = new ArrayList<>();
        for (int j=0;j<nb_players;j++){
            if (players.get(j).getCrown()){index =j;}
        }
        for (int i=index;i<nb_players;i++){
         alternateList.add(players.get(i));
        }
        for (int i=0;i<index;i++){
            alternateList.add(players.get(i));
        }
        return alternateList;
    }

    void clearCrown(){
        for (Player p : players){
            p.leaveCrown();
        }
    }
    void run(){
        showWinner(newGame());
    }
}
