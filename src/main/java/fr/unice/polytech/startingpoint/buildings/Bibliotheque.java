package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.Player;

public class Bibliotheque extends Prestige{

    public Bibliotheque(BuildingEnum b) {
        super(b);
    }

    public void useEffect(Player p){
        System.out.println("Effect Bibliotheque");
        p.drawCards(1);
    }
}