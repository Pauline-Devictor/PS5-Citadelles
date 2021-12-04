package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Player;

public class Thief extends Character {
    public Thief() {
        super(2, "Thief");
    }

    public void steal(Character robbed){
            robbed.setStolen();
    }

    @Override
    public void usePower(Player p) {
        setPlayer(p);
        Character robbed =p.chooseRob();
        steal(robbed);
        System.out.println("Thief has stolen " + robbed.getName() );
    }
}