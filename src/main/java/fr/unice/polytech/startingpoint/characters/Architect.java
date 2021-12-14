package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.List;
import java.util.Optional;


public class Architect extends Character {
    private List<Building> cards;

    public Architect() {
        super(CharacterEnum.Architect);
    }

    /**
     * Uses the Architect's power
     * @param b the current game's board
     */
    @Override
    public void usePower(Board b) {
        //Architect allows building 2 more buildings total =3
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            p.get().buildingArchitect();
            cards = p.get().drawCards(2);
            System.out.println(printEffect(p.get()));
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    /**
     * Prints the power effect
     * @param p the Architect's player
     * @return the String to print
     */
    @Override
    public String printEffect(Player p) {
        StringBuilder res = new StringBuilder(super.printEffect(p));
        res.append(" Il pourra construire 3 batiments ce tour-ci.");
        if (!cards.isEmpty()) {
            res.append(" Il a pioché ");
            for (Building b : cards) {
                res.append(b.getName()).append(", ");
            }
        }
        return res.toString();
    }
}
