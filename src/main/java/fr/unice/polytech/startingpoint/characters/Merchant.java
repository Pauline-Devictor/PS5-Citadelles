package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Building;
import fr.unice.polytech.startingpoint.District;

public class Merchant extends Character{
    public Merchant(){
        super(6,"Merchant");
    }
    @Override
    public void usePower(){
        int taxes=0;
        for (Building b:getPlayer().getBuildings()) {
            if (b.getBuilding().getDistrict() == District.Commercial){
                taxes+=1;
            }
            getPlayer().setTaxes(taxes);
        }
    }
}
