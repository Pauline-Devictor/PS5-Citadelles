package fr.unice.polytech.startingpoint.Characters;

import java.util.List;

public abstract class Character {
    protected int order;
    protected boolean available;
    protected String name;

    public int getOrder() {
        return order;
    }
    public boolean isAvailable(){return available;}
    public String getName(){return name;}
    public void isTaken(){available=false;}//un bot prend la carte
    public void setFree(){available=true;}//liberation de la carte


}
