package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

import static fr.unice.polytech.startingpoint.Display.showDonjonEffect;

/**
 * The type Donjon.
 */
public class Donjon extends Prestige {
    /**
     * Instantiates a new Donjon.
     */
    public Donjon() {
        super(BuildingEnum.Donjon);
    }

    public void useEffect(Player p) {
        printEffect(p);
    }

    @Override
    public void printEffect(Player p) {
        super.printEffect(p);
        showDonjonEffect(p);
    }
}
