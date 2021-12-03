package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.Player;

public class Observatory extends Prestige {

    public Observatory(BuildingEnum b) {
        super(b);
    }

    public void useEffect(Player p) {
        System.out.println("Effect Observatoire");
        p.drawCards(1);
    }
}
