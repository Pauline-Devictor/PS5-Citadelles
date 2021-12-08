package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public class Library extends Prestige {

    public Library() {
        super(BuildingEnum.Bibiliotheque);
    }

    public void useEffect(Player p) {
        System.out.println("Effect Bibliotheque");
        p.drawCards(2);
    }
}
