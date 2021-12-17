package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public class Graveyard extends Prestige{
    public Graveyard() {
        super(BuildingEnum.Graveyard);
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
