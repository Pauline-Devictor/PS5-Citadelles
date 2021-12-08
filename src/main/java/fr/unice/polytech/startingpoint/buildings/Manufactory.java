package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public class Manufactory extends Prestige {

    public Manufactory() {
        super(BuildingEnum.Manufacture);
    }

    public void useEffect(Player p) {
        if (p.getGold() >= 3 && p.getBoard().getPile().numberOfCards() >= 3) {
            p.refundGold(3);
            p.drawCards(3);
        }
    }
}
