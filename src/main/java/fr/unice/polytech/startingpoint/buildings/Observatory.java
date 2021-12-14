package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public class Observatory extends Prestige {

    public Observatory() {
        super(BuildingEnum.Observatoire);
    }

    public void useEffect(Player p) {
        printEffect(p);
        p.drawAndChoose(3, 1);
    }
}
