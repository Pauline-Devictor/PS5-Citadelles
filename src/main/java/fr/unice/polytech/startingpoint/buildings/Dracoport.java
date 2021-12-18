package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

/**
 * The type Dracoport.
 */
public class Dracoport extends Prestige {

    /**
     * Instantiates a new Dracoport.
     */
    public Dracoport() {
        super(BuildingEnum.CourDesMiracles);
    }

    /**
     * Give 2 points to the the player in variables
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
