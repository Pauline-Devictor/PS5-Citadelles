package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.King;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.ArrayList;
import java.util.List;

import static fr.unice.polytech.startingpoint.Board.*;

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

    void endOfGame() {
        System.out.println(printFormat("_____________________________________________________________________", ANSI_RED, ANSI_BOLD, ANSI_WHITE_BACKGROUND)
                + "\n\n" +
                printFormat("La partie est fini, calcul des bonus :", ANSI_BLACK, ANSI_BLUE_BACKGROUND));
        players.forEach(e -> board.showBonus(e.equals(first), e));
        System.out.println(printFormat("Le classement de fin de partie est le suivant :", ANSI_BLACK, ANSI_BLUE_BACKGROUND));
        board.showRanking();
        System.out.println("\n" + printFormat("_____________________________________________________________________", ANSI_RED, ANSI_BOLD, ANSI_WHITE_BACKGROUND)
                + "\n"
                + printFormat("Pour plus de details sur la fin de partie :", ANSI_YELLOW, ANSI_ITALIC));
        board.showBoard();
    }

    void run() {
        newGame();
        endOfGame();
    }

    void newGame() {
        boolean endOfGame = false;
        int turn = 0;
        while (!endOfGame) {
            turn++;
            board.showVariables(turn);

            getOrderPlayer();
            orderPlayers.forEach(Player::chooseRole);

            players.sort(Player.RoleOrder);
            for (Player p : players) {
                p.play();
                //crown → selection des roles (le dernier roi la récupère)
                if (p.getRole().getClass().equals(King.class)) {
                    first = p;
                }
                if (!endOfGame && p.getCity().size() >= 8)
                    first = p;
                if (p.getCity().size() >= 8 || turn >= 50) {
                    endOfGame = true;
                }
            }
            board.release();
        }
        players.forEach(e -> e.calculScore(e.equals(first)));
    }

    void getOrderPlayer() {
        List<Player> alternateList = new ArrayList<>();
        int index = orderPlayers.indexOf(first);
        alternateList.addAll(orderPlayers.subList(index, orderPlayers.size()));
        alternateList.addAll(orderPlayers.subList(0, index));
        orderPlayers = List.copyOf(alternateList);
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getOrderPlayers() {
        return orderPlayers;
    }
}
