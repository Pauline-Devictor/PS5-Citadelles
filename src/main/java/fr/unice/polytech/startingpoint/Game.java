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

    void showEndOfGame() {
        System.out.println(ANSI_BLUE_BACKGROUND + ANSI_BLACK + "Le classement de fin de partie est le suivant :" + ANSI_RESET);
        board.showRanking();
        System.out.println(ANSI_ITALIC + ANSI_YELLOW + "Pour plus de details sur la fin de partie :" + ANSI_RESET);
        board.showBoard();
    }

    void run() {
        newGame();
        showEndOfGame();
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
            }

            board.release();
            for (Player p : players) {
                if (p.getCity().size() >= 8 || turn > 50) {
                    endOfGame = true;
                    break;
                }
            }
        }
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
}
