package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;
import java.util.Random;

public class Thief extends Character {
    public Thief() {
        super(CharacterEnum.Thief);
    }

    /**
     * Uses the Thief's power :
     * Set the thief on a role, if the role is pick
     * Take the gold of the target
     *
     * @param b the current game's board
     */
    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            printEffect(p.get());
            Character c = chooseVictim(b);
            if (!c.isMurdered() && c.getClass() != Assassin.class)
                c.stoleBy(p.get());
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }


    /**
     * Chooses the Thief's victim
     *
     * @param board kills current game's board
     * @return the Thief's victim
     */
    public Character chooseVictim(Board board) {
        Random random = new Random();
        //exclu l'indice de l'assassin et le voleur
        // """Ponderation""" pour favoriser le vol de l'architect
        int victim = random.nextInt(9) + 2;
        if (victim > 7) victim = 6;
        return board.getCharacters().get(victim);
    }
}