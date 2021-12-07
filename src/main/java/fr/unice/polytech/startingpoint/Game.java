package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static fr.unice.polytech.startingpoint.Main.*;

public class Game {
    private final Board board;
    private final List<Player> players;
    private List<Player> orderPlayers;
    private Player first;

    Game(int nb_players) {
        board = new Board(nb_players);
        orderPlayers = List.copyOf(board.getPlayers());
        players = board.getPlayers();
        first = players.get(0);
    }

    public List<Player> getPlayers() {
        return players;
    }

    List<Player> determineWinner() {
        List<Player> list_players = getPlayers();
        if (list_players.size() > 0) {
            int max = list_players.get(0).getGoldScore();
            for (Player p : list_players) {
                if (max < p.getGoldScore())
                    max = p.getGoldScore();
            }
            int finalMax = max;
            list_players = list_players.stream().filter(e -> e.getGoldScore() == finalMax).toList();
        }
        return list_players;
    }

    void showWinner(List<Player> winners ){
        //TODO Affichage Classement
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

            getOrderPlayer();
            orderPlayers.forEach(Player::chooseRole);

            players.sort(Player.RoleOrder);
            for (Player p : players) {
                if (p.getRole().isPresent()) {
                    p.play();
                    //crown → selection des roles (le dernier roi la récupère)
                    if (p.getRole().get().getName().equals("King")) {
                        first = p;
                    }
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
    void getOrderPlayer() {
        List<Player> alternateList = new ArrayList<>();
        int index = orderPlayers.indexOf(first);
        alternateList.addAll(orderPlayers.subList(index, orderPlayers.size()));
        alternateList.addAll(orderPlayers.subList(0, index));
        orderPlayers = List.copyOf(alternateList);
    }

    void resetPlayer(){
        for (Player p : players){
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
