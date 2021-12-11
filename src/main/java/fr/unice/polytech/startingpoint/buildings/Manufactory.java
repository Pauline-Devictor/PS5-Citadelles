package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.ArrayList;
import java.util.List;

public class Manufactory extends Prestige {
    private List<Building> cards;

    public Manufactory() {
        super(BuildingEnum.Manufacture);
        cards = new ArrayList<>();
    }

    public void useEffect(Player p) {
        System.out.println("Use Effect Manufacture");
        if (p.getGold() >= 3 && p.getBoard().getPile().numberOfCards() >= 3) {
            p.refundGold(3);
            cards = p.drawCards(3);
            System.out.println(printEffect(p));
        }
    }

    @Override
    public String printEffect(Player p) {
        StringBuilder res = new StringBuilder(super.printEffect(p));
        res.append(" Il a defaussé 3 pieces d'or");
        if (!cards.isEmpty()) {
            res.append(" et a pioché ");
            for (Building b : cards) {
                res.append(b.getName()).append(", ");
            }
        }
        return res.toString();
    }
}
