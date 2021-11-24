package fr.unice.polytech.startingpoint;

import java.util.Scanner;

public class Main {

    public static void main(String... args) {
        int nb_players;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Combien voulez-vous de joueurs ?");
            nb_players = sc.nextInt();
            }while(nb_players>6  || nb_players<2);
        new Game(new Board(),nb_players).run();
    }

}
