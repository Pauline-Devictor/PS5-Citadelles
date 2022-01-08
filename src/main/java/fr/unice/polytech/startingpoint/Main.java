package fr.unice.polytech.startingpoint;

public class Main {

    public static void main(String... args) {
        Game game = new Game();
        game.run1000("HighScoreArchi", "HighScoreThief", "HighThiefManufacture", "Opportuniste", "RushArchiLab", "RushMerch");
        game.run1000("RushMerch", "RushMerch", "RushMerch", "RushMerch");
    }
}
