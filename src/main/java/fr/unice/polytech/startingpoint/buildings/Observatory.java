package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

/**
 * The type Observatory.
 */
public class Observatory extends Prestige {

    /**
     * Instantiates a new Observatory.
     */
    public Observatory() {
        super(BuildingEnum.Observatoire);
    }

    /**
     * Allow the player to draw 3 cards and choose one
     *
     * @param p the Player
     */
    @Override
    public void useEffect(Player p) {
        printEffect(p);
        p.drawAndChoose(3, 1);
    }
}
