package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public class MiracleCourtyard extends Prestige {

    public MiracleCourtyard(BuildingEnum courDesMiracles) {
        super(courDesMiracles);
    }

    @Override
    public void useEffect(Player p) {
        System.out.println("Use Effect Cour Des Miracles");

        System.out.println(printEffect(p));
    }
}
