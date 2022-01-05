package fr.unice.polytech.startingpoint;

import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;


public class Main {
    public static int nb_players = 6;

    public static void main(String... args) {
        Game game = new Game(nb_players);
        LOGGER.setLevel(Level.FINEST);
        game.run();
        LOGGER.setLevel(Level.CONFIG);
        game.run1000();
    }
}
