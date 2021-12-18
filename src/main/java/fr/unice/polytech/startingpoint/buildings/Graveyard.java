package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

/**
 * The type Graveyard. Not Fully functional yet
 */
public class Graveyard extends Prestige {
    /**
     * Instantiates a new Graveyard.
     */
    public Graveyard() {
        super(BuildingEnum.Graveyard);
    }

    public void useEffect(Player p) {
        printEffect(p);
    }

    @Override
    public void printEffect(Player p) {
        super.printEffect(p);
    }
}
