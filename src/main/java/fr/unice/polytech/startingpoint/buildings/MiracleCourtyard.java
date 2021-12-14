package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public class MiracleCourtyard extends Prestige {

    public MiracleCourtyard(BuildingEnum courDesMiracles) {
        super(courDesMiracles);
    }

    @Override
    public void useEffect(Player p) {
        printEffect(p);
    }
}
