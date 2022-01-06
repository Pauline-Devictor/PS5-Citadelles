package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.List;
import java.util.Optional;

import static fr.unice.polytech.startingpoint.Display.showArchitectEffect;


public class Architect extends Character {
    private List<Building> cards;

    public Architect() {
        super(CharacterEnum.Architect);
    }

    /**
     * Uses the Architect's power :
     * Draw 2 Cards and raise the number of buildings available for the turn to 3
     *
     * @param b the current game's board
     */
    @Override
    public void usePower(Board b) {
        //Architect allows building 2 more buildings total =3
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            p.get().buildingArchitect();
            cards = p.get().drawAndChoose(2, 2);
            printEffect(p.get());
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    /**
     * Prints the power effect
     *
     * @param p the Architect's player
     */
    @Override
    public void printEffect(Player p) {
        super.printEffect(p);
        showArchitectEffect(p, cards);
    }
}
