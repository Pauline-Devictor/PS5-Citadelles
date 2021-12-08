package fr.unice.polytech.startingpoint;

public class Main {
    public static int nb_players = 6;

    public static void main(String... args) {
        /*int nb_players;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Combien voulez-vous de joueurs ?");
            nb_players = sc.nextInt();
        } while (nb_players > 6 || nb_players < 2);*/
        for (int i = 0; i < 170; i++)
            new Game(nb_players).run();
    }
}
