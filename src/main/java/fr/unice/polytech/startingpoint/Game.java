package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.King;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.*;
import java.util.logging.Formatter;
import java.util.logging.*;

import static fr.unice.polytech.startingpoint.Board.*;
import static fr.unice.polytech.startingpoint.Main.nb_players;
import static fr.unice.polytech.startingpoint.strategies.Player.PointsOrder;

/**
 * The type Game.
 */
public class Game {
    public static Logger LOGGER = Logger.getLogger(Game.class.getName());
    private Board board;
    private List<Player> players;
    private List<Player> orderPlayers;
    private Player first;

    /**
     * Instantiates a new Game.
     *
     * @param nb_players the nb players
     */
    Game(int nb_players) {
        initBoard(nb_players);

        ConsoleHandler show = new ConsoleHandler();
        LOGGER.addHandler(show);
        show.setLevel(Level.FINEST);

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

    void initBoard(int nb_players) {
        board = new Board(nb_players);
        orderPlayers = List.copyOf(board.getPlayers());
        players = board.getPlayers();
        first = players.get(0);
    }

    void run1000() {
        Map<String, Integer> results = new TreeMap<>();
        for (int i = 0; i < 1000; i++) {
            newGame();
            players.sort(PointsOrder);
            if (results.containsKey(players.get(0).getName()))
                results.put(players.get(0).getName(), results.get(players.get(0).getName()) + 1);
            else
                results.put(players.get(0).getName(), 1);
        }
        board.showStats(results);
    }

    /**
     * Start the game and keep going until game is over
     */
    void newGame() {
        initBoard(nb_players);
        boolean endOfGame = false;
        int turn = 0;
        while (!endOfGame) {
            turn++;
            board.showVariables(turn);

            //Phase de Choix des Roles
            getOrderPlayer();
            orderPlayers.forEach(Player::chooseRole);
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
