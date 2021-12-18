package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

/**
 * The type Prestige.
 */
public abstract class Prestige extends Building {

    /**
     * Instantiates a new Prestige.
     *
     * @param b the b
     */
    public Prestige(BuildingEnum b) {
        super(b);
    }

    /**
     * Use effect.
     *
     * @param p the p
     */
    public abstract void useEffect(Player p);

    /**
     * Print effect.
     *
     * @param p the p
     */
    public void printEffect(Player p) {
        p.getBoard().showPrestigeEffect(p, this);
    }

}
