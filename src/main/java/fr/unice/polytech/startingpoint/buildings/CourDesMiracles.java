package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.Player;

public class CourDesMiracles extends Prestige {

    public CourDesMiracles(BuildingEnum courDesMiracles) {
        super(courDesMiracles);
    }

    @Override
    public void useEffect(Player p) {
        System.out.println("Effect Cour Des Miracles");
    }
}
