package fr.unice.polytech.startingpoint;

import java.util.List;

public abstract class Character {
     protected int order;
     protected boolean taken;

     int getOrder() {
        return order;
    }
    boolean isAvailable(){
         return taken;
    }
}
