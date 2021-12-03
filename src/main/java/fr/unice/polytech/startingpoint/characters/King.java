package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.District;

public class King extends Character{
    public King(){
        super(4,"King");
    }
    @Override
    public void usePower(){
        int taxes=0;
        for (Building b:getPlayer().getCardHand()) {
            if (b.getBuilding().getDistrict() == District.Noble){
            taxes++;
            }
            getPlayer().setTaxes(taxes);
        }
    }
}
