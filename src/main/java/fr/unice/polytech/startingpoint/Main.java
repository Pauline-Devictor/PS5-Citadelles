package fr.unice.polytech.startingpoint;

import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;


public class Main {

    public static void main(String... args) {
        Game game = new Game();
        LOGGER.setLevel(Level.FINEST);
        game.run();
        LOGGER.setLevel(Level.CONFIG);
        game.run1000("HighScoreArchi", "HighScoreThief", "HighThiefManufactory", "Opportuniste", "RushArchiLab", "RushMerch");
        game.run1000("RushMerch", "RushMerch", "RushMerch", "RushMerch");
        game.run1000("Opportuniste", "Opportuniste", "Opportuniste", "Opportuniste", "RushArchiLab", "RushMerch");
    }
}
