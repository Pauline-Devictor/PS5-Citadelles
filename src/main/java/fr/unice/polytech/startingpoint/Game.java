package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.King;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.*;

import static fr.unice.polytech.startingpoint.Board.*;

/**
 * The type Game.
 */
public class Game {
    public static Logger LOGGER = Logger.getLogger(Game.class.getName());
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

        LOGGER.setLevel(Level.ALL);
        ConsoleHandler show = new ConsoleHandler();
        show.setLevel(Level.FINEST);
        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(show);
        Formatter testFormat = new SimpleFormatter() {
            private static final String format = "%3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        new Date(lr.getMillis()),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage()
                );
            }
        };
        show.setFormatter(testFormat);
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

        LOGGER.finest(printFormat("_____________________________________________________________________", ANSI_RED, ANSI_BOLD, ANSI_WHITE_BACKGROUND) + "\n\n" +
                printFormat("La partie est fini, calcul des bonus :", ANSI_BLACK, ANSI_BLUE_BACKGROUND));
        players.forEach(e -> board.showBonus(e.equals(first), e));
        LOGGER.finer(printFormat("Le classement de fin de partie est le suivant :", ANSI_BLACK, ANSI_BLUE_BACKGROUND));
        board.showRanking();
        LOGGER.finest("\n" + printFormat("_____________________________________________________________________", ANSI_RED, ANSI_BOLD, ANSI_WHITE_BACKGROUND)
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
            //LOGGER.fine("\n");
            //System.out.println();
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
