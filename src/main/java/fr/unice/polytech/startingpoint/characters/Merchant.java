package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.District;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;

import static fr.unice.polytech.startingpoint.District.Commercial;
import static fr.unice.polytech.startingpoint.District.Noble;

public class Merchant extends Character {
    public Merchant() {
        super(6, "Merchant");
    }

    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            collectTaxes(p.get(), Commercial);
            p.get().takeMoney(1);
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");

    }

}
