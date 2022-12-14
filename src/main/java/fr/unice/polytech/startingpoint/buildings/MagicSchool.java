package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

import static fr.unice.polytech.startingpoint.Display.showMagicSchoolEffect;

/**
 * The type Magic school.
 */
public class MagicSchool extends Prestige {

    /**
     * Instantiates a new Magic school.
     */
    public MagicSchool() {
        super(BuildingEnum.EcoleDeMagie);
    }

    public void useEffect(Player p) {
        printEffect(p);
    }

    @Override
    public void printEffect(Player p) {
        super.printEffect(p);
        showMagicSchoolEffect(p);
    }
}
