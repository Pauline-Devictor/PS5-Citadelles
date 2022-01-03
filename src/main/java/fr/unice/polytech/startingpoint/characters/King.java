package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;

import static fr.unice.polytech.startingpoint.buildings.District.Noble;

public class King extends Character {

    public King() {
        super(CharacterEnum.King);
    }

    /**
     * Uses the King's power :
     * Collect Taxes from Noble Districts
     *
     * @param b the current game's board
     */
    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            printEffect(p.get());
            collectTaxes(p.get(), Noble);
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    /**
     * Prints the power effect
     *
     * @param p the King's player
     */
    @Override
    public void printEffect(Player p) {
        super.printEffect(p);
        p.getBoard().showKingEffect(p);
    }
}
