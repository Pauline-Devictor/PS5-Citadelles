package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public class MiracleCourtyard extends Prestige {

    public MiracleCourtyard() {
        super(BuildingEnum.CourDesMiracles);
    }

    @Override
    public void useEffect(Player p) {
        printEffect(p);
    }
}
