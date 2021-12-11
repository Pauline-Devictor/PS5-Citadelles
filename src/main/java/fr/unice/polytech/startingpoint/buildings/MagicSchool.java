package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

public class MagicSchool extends Prestige {

    public MagicSchool() {
        super(BuildingEnum.EcoleDeMagie);
    }

    public void useEffect(Player p) {
        System.out.println(printEffect(p));
    }

    @Override
    public String printEffect(Player p) {
        return super.printEffect(p) + "Il recupere une piece de plus des taxes";
    }
}
