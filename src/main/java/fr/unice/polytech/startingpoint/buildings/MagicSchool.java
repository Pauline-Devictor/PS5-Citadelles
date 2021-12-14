package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public class MagicSchool extends Prestige {

    public MagicSchool() {
        super(BuildingEnum.EcoleDeMagie);
    }

    public void useEffect(Player p) {
        printEffect(p);
    }

    @Override
    public void printEffect(Player p) {
        super.printEffect(p);
        p.getBoard().showMagicSchoolEffect(p);
    }
}
