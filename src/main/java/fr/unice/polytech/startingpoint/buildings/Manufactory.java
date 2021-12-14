package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class Manufactory extends Prestige {
    private List<Building> cards;

    public Manufactory() {
        super(BuildingEnum.Manufacture);
        cards = new ArrayList<>();
    }

    public void useEffect(Player p) {
        printEffect(p);
        if (p.getGold() >= 3 && p.getBoard().getPile().numberOfCards() >= 3) {
            p.refundGold(3);
            cards = p.drawAndChoose(3, 3);
        }
    }

    @Override
    public void printEffect(Player p) {
        super.printEffect(p);
        p.getBoard().showManufactoryEffect(p, cards);
    }
}
