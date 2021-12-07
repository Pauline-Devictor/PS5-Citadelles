package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public class Observatory extends Prestige {

    public Observatory(BuildingEnum b) {
        super(b);
    }

    public void useEffect(Player p) {
        p.drawCards(1);
    }
}
