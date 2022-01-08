package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

import static fr.unice.polytech.startingpoint.Display.printPrestigePoint;

/**
 * The type Dracoport.
 */
public class Dracoport extends Prestige {

    /**
     * Instantiates a new Dracoport.
     */
    public Dracoport() {
        super(BuildingEnum.Dracoport);
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
        printPrestigePoint(p, this);
    }
}
