package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.King;
import fr.unice.polytech.startingpoint.csv.CsvRead;
import fr.unice.polytech.startingpoint.csv.CsvWrite;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.text.DateFormat;
import java.util.*;
import java.util.logging.Formatter;
import java.util.logging.*;

import static fr.unice.polytech.startingpoint.Board.*;
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
    private final CsvWrite writer = new CsvWrite();
    private final CsvRead reader = new CsvRead();
    private final Date today = new Date();


    /**
     * Instantiates a new Game.
     */
    Game() {
        initBoard();

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

    void initBoard(String... namePlayers) {
        board = new Board(namePlayers);
        orderPlayers = List.copyOf(board.getPlayers());
        players = board.getPlayers();
        first = players.get(0);
    }

    void run1000(String... namePlayers) {
        Map<String, int[]> results = new TreeMap<>();
        for (int i = 0; i < 1000; i++) {
            initBoard(namePlayers);
            newGame();
            results = calculStats(results);
        }
        board.showStats(results);
        updateResults(results);
    }

    /**
     * Compute the stats of all players
     *
     * @return stats of players
     */
    Map<String, int[]> calculStats(Map<String, int[]> stats) {
        players.sort(PointsOrder);
        getPlayers().forEach(e -> {
            int[] tmp = stats.getOrDefault(e.getName(), new int[4]);
            stats.put(e.getName(), calculStatsPlayer(tmp, e));
        });
        return stats;
    }

    /**
     * Use the SORTED list of players to calcul the stats of a player
     * Assumption is made that the array is has a length of 4, in this order :
     * Score Cumulés : 0
     * Parties Gagnées : 1
     * Parties Nulles : 2
     * Nombre de Parties : 3
     *
     * @param stats former stats of the player
     * @param p     Player who need to update his stats
     * @return updated stats of the player
     */
    int[] calculStatsPlayer(int[] stats, Player p) {
        stats[0] += p.getScore();
        //Si le Score est égal a celui du premier
        if (p.getScore() == getPlayers().get(0).getScore()) {
            if (getPlayers().get(0).getScore() != getPlayers().get(1).getScore()) //Il n'y a pas d'égalité
                stats[1]++; //Il a donc gagné
            else
                stats[2]++;//il est donc égalité
        }
        stats[3]++;
        return stats;
    }

    /**
     * Start the game and keep going until game is over
     * Reinitialize the stats.csv datas
     * Create a Board and play a full game with it
     */
    void newGame() {
        writer.write("Nom;ScoreMoyen;Victoire;Egalite;NbParties");
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

    /**
     * Get the date time
     * @return current Date
     */
    public String getDate(){
        DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.MEDIUM);
        return(mediumDateFormat.format(today));
    }

    /**
     * Update stats results in results.csv
     * @param results of all games
     */
    public void updateResults(Map<String, int[]> results){
        writer.append(getDate());
        writer.appendStats(results);
        writer.save();
        reader.printCsv("save/results.csv");
    }
}
