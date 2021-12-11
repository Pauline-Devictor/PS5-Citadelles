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
            //TODO Test Main vide
            card = p.discardCard();
            p.takeMoney(1);
            //TODO Dans le board ?
            System.out.println(printEffect(p));
        }
    }

    @Override
    public String printEffect(Player p) {
        String res = super.printEffect(p);
        if (isNull(card))
            res += " Il a recuperé 1 piece d'or ";
        else
            res += " Il a recuperé 1 piece d'or et a defaussé " + card.getName();
        return res;
    }
}
