package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Building;
import fr.unice.polytech.startingpoint.District;

public class Bishop extends Character{
    public Bishop(){
        super(5,"Bishop");
    }
    @Override
    public void usePower(){
        int taxes=0;
        for (Building b:getPlayer().getBuildings()) {
            if (b.getBuilding().getDistrict() == District.Religion){
                taxes+=1;
            }
            getPlayer().setTaxes(taxes);
        }
    }
}