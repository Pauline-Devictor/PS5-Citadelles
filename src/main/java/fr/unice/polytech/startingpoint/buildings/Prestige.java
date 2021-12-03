package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.Player;

public abstract class Prestige extends Building {


    public Prestige(BuildingEnum b) {
        super(b);
    }

    public abstract void useEffect(Player p);
}
