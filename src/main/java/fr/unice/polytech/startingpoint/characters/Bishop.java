package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;

import static fr.unice.polytech.startingpoint.buildings.District.Religion;

public class Bishop extends Character {
    public Bishop() {
        super(CharacterEnum.Bishop);
    }

    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            collectTaxes(p.get(), Religion);
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }
}