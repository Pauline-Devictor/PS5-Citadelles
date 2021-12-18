package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public class University extends Prestige {

    public University() {
        super(BuildingEnum.CourDesMiracles);
    }

    @Override
    public void useEffect(Player p) {
        printEffect(p);
        p.bonusPoints(2);
    }

    @Override
    public void printEffect(Player p) {
        super.printEffect(p);
        p.getBoard().printPrestigePoint(p, this);
    }
}
