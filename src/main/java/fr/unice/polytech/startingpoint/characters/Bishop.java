package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;

import static fr.unice.polytech.startingpoint.buildings.District.Religion;

public class Bishop extends Character {

    public Bishop() {
        super(CharacterEnum.Bishop);
    }

    /**
     * Uses the Bishop's power
     * Collect taxes of Religion Buildings
     *
     * @param b the current game's board
     */
    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            printEffect(p.get());
            collectTaxes(p.get(), Religion);
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }
}