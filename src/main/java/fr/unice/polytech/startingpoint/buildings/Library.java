package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

/**
 * The type Library.
 */
public class Library extends Prestige {

    /**
     * Instantiates a new Library.
     */
    public Library() {
        super(BuildingEnum.Bibiliotheque);
    }

    /**
     * Allow the player to draw 2 Cards
     *
     * @param p the p
     */
    public void useEffect(Player p) {
        printEffect(p);
        p.drawAndChoose(2, 2);
    }
}
