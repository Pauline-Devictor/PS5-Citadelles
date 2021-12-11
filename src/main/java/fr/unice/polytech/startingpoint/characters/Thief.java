package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;

public class Thief extends Character {
    public Thief() {
        super(CharacterEnum.Thief);
    }

    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            System.out.println(printEffect(p.get()));
            Character c = p.get().chooseVictim();
            if (!c.isMurdered() && c.getClass() != Assassin.class)
                c.setThief(p);
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }
}