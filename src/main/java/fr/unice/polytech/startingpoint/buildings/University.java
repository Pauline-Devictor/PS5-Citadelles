package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

/**
 * The type University.
 */
public class University extends Prestige {

    /**
     * Instantiates a new University.
     */
    public University() {
        super(BuildingEnum.Universite);
    }

    /**
     * Give 2 points to the player in variables
     *
     * @param p the Player
     */
    @Override
    public void useEffect(Player p) {
        printEffect(p);
        p.bonusPoints(2);
    }

    @Override
    public void printEffect(Player p) {
        super.printEffect(p);
        p.getBoard().printPrestigePoint(p, this);
    }
}
