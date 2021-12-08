package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public class Laboratory extends Prestige {

    public Laboratory() {
        super(BuildingEnum.Laboratoire);
    }

    @Override
    public void useEffect(Player p) {
        if (p.getBoard().getBank().getGold() >= 1) {
            p.discardCard();
            p.takeMoney(1);
        }
    }
}
