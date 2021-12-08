package fr.unice.polytech.startingpoint;

public class Main {
    public static int nb_players = 4;

    public static void main(String... args) {
        for (int i = 0; i < 170; i++)
            new Game(nb_players).run();
    }
}
