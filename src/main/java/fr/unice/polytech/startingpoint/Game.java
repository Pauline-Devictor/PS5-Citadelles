package fr.unice.polytech.startingpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static fr.unice.polytech.startingpoint.Main.*;

public class Game {
    private final Board board;
    private final List<Player> players;
    private final int  nb_players;

    Game(int nb_players){
        this.nb_players = nb_players;
        board = new Board();
        players = new ArrayList<>();
        Random r = new Random();
        for(int i=1;i<=nb_players;i++){
            players.add(new Player(board, String.valueOf(i),Strategies.pickAStrat(r.nextInt(3))));
        }
        board.setPlayers(players,nb_players);
        players.get(0).setCrown(true);
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
        if(winners.size()>1)
            System.out.println(ANSI_BLUE_BACKGROUND+ANSI_BLACK+"Les gagnants sont :"+ANSI_RESET);
        else
            System.out.println(ANSI_BLUE_BACKGROUND+ANSI_BLACK+"Le gagnant est :"+ANSI_RESET);
        winners.forEach(System.out::println);
    }

    List<Player> newGame() {
        boolean endOfGame = false;
        int turn = 0;
        while (!endOfGame) {
            AtomicInteger res = new AtomicInteger();
            players.forEach(e -> res.addAndGet(e.getGold()));
            System.out.println(ANSI_RED_BACKGROUND + ANSI_BLACK + "Tour " + (++turn) + ":" + ANSI_RESET +
                    " Cartes Restantes : " + board.getPile().numberOfCards() + " Or Dans la Banque : " + board.getBank().getGold() + " Or Joueurs :" + res);

            List<Player> roleOrder = getOrderPlayer();
            roleOrder.forEach(Player::chooseRole);

            players.sort(Player.RoleOrder);
            //System.out.println(roleOrder+" \n Players :"+players);

            for (Player p : players) {
                if (p.getRole().isPresent()) {
                    p.play();
                    //crown → selection des roles (le dernier roi la récupère)
                    if (p.getRole().get().getName().equals("King"))
                        p.setCrown(true);
                }
            }
            resetPlayer();
            board.setAllFree();

            for (Player p : players) {
                if (p.getCity().size() >= 8 || turn > 50) {
                    endOfGame = true;
                    break;
                }
            }
        }
        return determineWinner();
    }

    //TODO Ordre des Choix, Couronne puis a gauche du joueur actuel
    List<Player> getOrderPlayer() {
        int index = 0;
        List<Player> alternateList = new ArrayList<>();
        for (int j = 0; j < nb_players; j++) {
            if (players.get(j).getCrown()) {
                index = j;
            }
        }
        for (int i = index; i < nb_players; i++) {
            alternateList.add(players.get(i));
        }
        for (int i = 0; i < index; i++) {
            alternateList.add(players.get(i));
        }
        return alternateList;
    }

    void resetPlayer(){
        for (Player p : players){
            p.setCrown(false);
            p.setNbBuildable(1);
            p.setTaxes(0);
            p.removeRole();
        }
    }
    void run(){
        showWinner(newGame());
    }

    public Board getBoard() {
        return board;
    }
}
