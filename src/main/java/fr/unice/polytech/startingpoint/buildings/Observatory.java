package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.List;

public class Observatory extends Prestige {

    public Observatory() {
        super(BuildingEnum.Observatoire);
    }

    public void useEffect(Player p) {
        System.out.println("Use Effect Observatoire");
        List<Building> tmp = p.drawAndChoose(3, 1);
        tmp.forEach(building -> p.getCardHand().add(building));
        System.out.println(printEffect(p));
    }
}
