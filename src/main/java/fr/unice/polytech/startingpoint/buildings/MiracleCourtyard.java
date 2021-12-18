package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

/**
 * The type Miracle courtyard.
 */
public class MiracleCourtyard extends Prestige {

    /**
     * Instantiates a new Miracle courtyard.
     */
    public MiracleCourtyard() {
        super(BuildingEnum.CourDesMiracles);
    }

    @Override
    public void useEffect(Player p) {
        printEffect(p);
    }
}
