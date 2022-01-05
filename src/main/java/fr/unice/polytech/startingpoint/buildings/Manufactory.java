package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Manufactory.
 */
public class Manufactory extends Prestige {
    private List<Building> cards;

    /**
     * Instantiates a new Manufactory.
     */
    public Manufactory() {
        super(BuildingEnum.Manufacture);
        cards = new ArrayList<>();
    }

    /**
     * If the player pay 3 Gold, gave him 3 cards
     *
     * @param p the Player
     */
    @Override
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
        p.getBoard().showManufactureEffect(p, cards);
    }
}
