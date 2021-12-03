package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Player;

public class Architect extends Character {
    public Architect() {
        super(7, "Architect");
    }

    @Override
    public void usePower(Player p) {
        //Architect allow building 2 more buildings total =3
        p.setNbBuildable(3);
        p.drawCards(2);
    }
}
