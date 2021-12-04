package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Player;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.District;

public class Condottiere extends Character {
    public Condottiere() {
        super(8, "Condottiere");
    }

    public void collectTaxes(Player p){
        int taxes = 0;
        for (Building b : p.getCardHand()) {
            if (b.getBuilding().getDistrict() == District.Military) {
                taxes++;
            }
            p.setTaxes(taxes);
        }
    }

    public void destroyBuilding(Player victim){

    }

    @Override
    public void usePower(Player p) {
        setPlayer(p);
        collectTaxes(p);
    }
}