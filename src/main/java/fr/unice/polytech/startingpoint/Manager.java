package fr.unice.polytech.startingpoint;

import java.util.List;

public class Manager {
    Board board;
    List<Player> players;
    public Manager(List<Player> p){
        board = new Board();
        players = p;
    }
    void giveRole(){
        for(Player p:players){
            p.chooseRole();
            p.getRole().setPlayer(p);
        }
    }
}
