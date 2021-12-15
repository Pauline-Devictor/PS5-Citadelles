package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

import static java.util.Objects.isNull;

public class Laboratory extends Prestige {
    private Building card;

    public Laboratory() {
        super(BuildingEnum.Laboratoire);
        card = null;
    }

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
