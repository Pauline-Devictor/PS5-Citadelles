package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;

public class Assassin extends Character {
    public Assassin() {
        super(CharacterEnum.Assassin);
    }

    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            Character c = p.get().chooseVictim();
            c.setMurdered(true);
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }
}
