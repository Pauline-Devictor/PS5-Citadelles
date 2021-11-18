package fr.unice.polytech.startingpoint.Characters;

import java.util.List;

public abstract class Character {
     protected int order;
     protected boolean available;

     public int getOrder() {
        return order;
    }
     boolean isAvailable(){
         return available;
    }
}
