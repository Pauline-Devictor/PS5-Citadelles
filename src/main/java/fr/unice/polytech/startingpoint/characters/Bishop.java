package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Player;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.District;

public class Bishop extends Character {
    public Bishop() {
        super(5, "Bishop");
    }

    @Override
    public void usePower(Player p) {
        int taxes = 0;
        for (Building b : p.getCardHand()) {
            if (b.getBuilding().getDistrict() == District.Religion) {
                taxes++;
            }
            p.setTaxes(taxes);
        }
    }
}