package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.District;

import java.util.Optional;

import static fr.unice.polytech.startingpoint.District.*;

public class King extends Character {
    public King() {
        super(4, "King");
    }

    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            collectTaxes(p.get(), Noble);
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }
}
