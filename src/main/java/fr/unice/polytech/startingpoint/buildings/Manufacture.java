package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public class Manufacture extends Prestige {

    public Manufacture(BuildingEnum b) {
        super(b);
    }

    public void useEffect(Player p) {
        System.out.println("Manufacture");
        if (p.getGold() >= 3 && p.getBoard().getPile().numberOfCards() >= 3) {
            System.out.println("Condtion");
            p.refundGold(3);
            p.drawCards(3);
        }
    }
}
