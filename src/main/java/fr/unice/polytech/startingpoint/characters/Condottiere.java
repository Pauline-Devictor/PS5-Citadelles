package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.District;

public class Condottiere extends Character{
    public Condottiere(){
        super(8,"Condottiere");
   }
    @Override
    public void usePower(){
        int taxes=0;
        for (Building b:getPlayer().getCardHand()) {
            if (b.getBuilding().getDistrict() == District.Military){
                taxes++;
            }
            getPlayer().setTaxes(taxes);
        }
    }
}