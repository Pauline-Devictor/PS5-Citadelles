package fr.unice.polytech.startingpoint;

import java.util.Date;
import java.util.logging.*;

import static fr.unice.polytech.startingpoint.Board.ANSI_RESET;
import static fr.unice.polytech.startingpoint.Game.LOGGER;

public class Main {
    public static int nb_players = 4;

    public static void main(String... args) {
        //for (int i = 0; i < 1000; i++)
        new Game(nb_players).run();
    }
}
