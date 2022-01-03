package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.King;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.ArrayList;
import java.util.List;

import static fr.unice.polytech.startingpoint.Board.*;

/**
 * The type Game.
 */
public class Game {
    private final Board board;
    private final List<Player> players;
    private List<Player> orderPlayers;
    private Player first;

    /**
     * Instantiates a new Game.
     *
     * @param nb_players the nb players
     */
    Game(int nb_players) {
        board = new Board(nb_players);
        orderPlayers = List.copyOf(board.getPlayers());
        players = board.getPlayers();
        first = players.get(0);
    }

    /**
     * @return players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Print the end game details
     */
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
        board.writeWinner();
    }

    /**
     * Launch the game
     */

    void run() {
        newGame();
        endOfGame();
    }

    /**
     * Start the game and keep going until game is over
     */
    void newGame() {
        boolean endOfGame = false;
        int turn = 0;
        while (!endOfGame) {
            turn++;
            board.showVariables(turn);

            //Phase de Choix des Roles
            getOrderPlayer();
            orderPlayers.forEach(Player::chooseRole);
            System.out.println();
            players.sort(Player.RoleOrder);

            //Phase de Jeu
            for (Player p : players) {
                p.play();
                if (p.getRole().getClass().equals(King.class)) {
                    first = p;
                }
                if (!endOfGame && p.getCity().size() >= 8)
                    first = p;
                //Si le jeu est fini
                if (p.getCity().size() >= 8 || turn >= 50) {
                    endOfGame = true;
                }
            }
            //On reset les personnages
            board.release();
        }
        //Calcul des points bonus
        players.forEach(e -> e.calculScore(e.equals(first)));
    }

    /**
     * Get the right order in order to choose Roles, depending on the King of the last turn
     */
    void getOrderPlayer() {
        List<Player> alternateList = new ArrayList<>();
        int index = orderPlayers.indexOf(first);
        alternateList.addAll(orderPlayers.subList(index, orderPlayers.size()));
        alternateList.addAll(orderPlayers.subList(0, index));
        orderPlayers = List.copyOf(alternateList);
    }

    /**
     * Gets board.
     *
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Return the choosing order
     *
     * @return orderPlayers
     */
    public List<Player> getOrderPlayers() {
        return orderPlayers;
    }
}
