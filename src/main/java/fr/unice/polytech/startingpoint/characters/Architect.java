package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;


public class Architect extends Character {
    public Architect() {
        super(7, "Architect");
    }

    @Override
    public void usePower(Board b) {
        //Architect allow building 2 more buildings total =3
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            p.get().setNbBuildable(3);
            p.get().drawCards(2);
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }
}
