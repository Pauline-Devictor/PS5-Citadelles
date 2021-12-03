package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.Player;

public class Observatory extends Building{

    public Observatory(BuildingEnum b) {
        super(b);
    }

    public void useEffect(Player p){
        if(p.getGold()>=3 && p.getBoard().getPile().numberOfCards()>=3){
            //System.out.println("Effect Manufacture");
            p.getBoard().getBank().refundGold(3);
            p.drawCards(3);
        }
    }
}
