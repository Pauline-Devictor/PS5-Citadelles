package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Player;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.District;

public class Merchant extends Character {
    public Merchant() {
        super(6, "Merchant");
    }

    @Override
    public void usePower(Player p) {
        int taxes = 0;
        for (Building b : p.getCardHand()) {
            if (b.getBuilding().getDistrict() == District.Commercial) {
                taxes++;
            }
            p.setTaxes(taxes);
        }
    }
}
