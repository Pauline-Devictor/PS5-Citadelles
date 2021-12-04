package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Player;

public class Assassin extends Character {
    public Assassin() {
        super(1, "Assassin");
    }

    void power(Character victim) {
        victim.setMurdered();
        System.out.println("Character " + victim.getName() + " has been killed");
    }

    @Override
    public void usePower(Player p) {
        setPlayer(p);
        Character victim = p.chooseVictim();
        power(victim);
    }
}
