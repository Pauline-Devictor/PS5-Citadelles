package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

import static java.util.Objects.isNull;

/**
 * The type Laboratory.
 */
public class Laboratory extends Prestige {
    private Building card;

    /**
     * Instantiates a new Laboratory.
     */
    public Laboratory() {
        super(BuildingEnum.Laboratoire);
        card = null;
    }

    /**
     * If the player discard one card, give him one gold
     *
     * @param p the player
     */
    @Override
    public void useEffect(Player p) {
        if (p.getBoard().getBank().getGold() >= 1 && p.getCardHand().size() > 0) {
            card = p.discardCard();
            if (!isNull(card))
                p.takeMoney(1);
        }
        printEffect(p);
    }

    @Override
    public void printEffect(Player p) {
        super.printEffect(p);
        p.getBoard().showLaboratoryEffect(p, card);
    }
}
