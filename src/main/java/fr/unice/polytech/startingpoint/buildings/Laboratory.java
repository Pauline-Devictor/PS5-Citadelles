package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.Player;

import java.util.Optional;

public class Laboratory extends Prestige{

    public Laboratory(BuildingEnum b) {
        super(b);
    }

    @Override
    public void useEffect(Player p) {
        if(p.getBoard().getBank().getGold()>=1){
            p.discardCard(Optional.empty());
            p.getBoard().getBank().withdrawGold(1);
        }
    }
}
