package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Observatory extends Prestige {

    public Observatory() {
        super(BuildingEnum.Observatoire);
    }

    public void useEffect(Player p) {
        Optional<Building> tmp = p.drawAndChoose(3);
        tmp.ifPresent(building -> p.getCardHand().add(building));
        System.out.println(printEffect(p));
    }
}
