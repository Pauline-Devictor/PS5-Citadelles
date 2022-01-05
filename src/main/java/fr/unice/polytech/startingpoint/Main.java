package fr.unice.polytech.startingpoint;

import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;

import fr.unice.polytech.startingpoint.csv.CsvRead;
import fr.unice.polytech.startingpoint.csv.CsvWrite;

public class Main {
    public static int nb_players = 6;
    private static CsvWrite writer = new CsvWrite();

    public static void main(String... args) {
        Game game = new Game(nb_players);
        LOGGER.setLevel(Level.FINEST);
        game.run();
        LOGGER.setLevel(Level.CONFIG);
        game.run1000();
        writer.save();


    }
}
