package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public abstract class Prestige extends Building {

    public Prestige(BuildingEnum b) {
        super(b);
    }

    public abstract void useEffect(Player p);

    public String printEffect(Player p) {
        return p.getName() + " a utilisé : " + getName() + ".";
    }

}
