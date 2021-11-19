package fr.unice.polytech.startingpoint.characters;

public abstract class Character {
     protected int order;
     protected boolean taken;

     public int getOrder() {
        return order;
    }
     boolean isAvailable(){
         return taken;
    }
}
