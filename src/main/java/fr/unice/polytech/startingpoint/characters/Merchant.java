package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;

import static fr.unice.polytech.startingpoint.buildings.District.Commercial;

public class Merchant extends Character {
    public Merchant() {
        super(CharacterEnum.Merchant);
    }

    /**
     * Uses the Merchant's power
     * @param b the current game's board
     */
    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            System.out.println(printEffect(p.get()));
            collectTaxes(p.get(), Commercial);
            p.get().takeMoney(1);
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    /**
     * Prints the power effect
     * @param p the Merchant's player
     * @return the String to print
     */
    @Override
    public String printEffect(Player p) {
        String res = super.printEffect(p);
        if (!p.getBoard().getBank().isEmpty())
            res += " Il a recupere une piece de bonus. ";
        return res;
    }
}
