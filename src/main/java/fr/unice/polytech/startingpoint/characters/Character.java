package fr.unice.polytech.startingpoint.characters;
import fr.unice.polytech.startingpoint.Player;


public abstract class Character {
    private final int order;
    protected boolean available;
    private final String name;
    private boolean isMurdered;

    public Character(int order, String name) {
        this.order = order;
        this.name = name;
        available = true;
        isMurdered = false;

    }

    public int getOrder() {
        return order;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getName() {
        return name;
    }

    public void isTaken() {
        available = false;
    }//un bot prend le personnage

    @Override
    public String toString() {
        return name;
    }

    public void resetRole() {
        //Not killed anymore
        isMurdered = false;
        //Free the character after each turn
        available = true;
    }

    public void setMurdered() {
        isMurdered = true;
    }

    public boolean gotMurdered() {
        return isMurdered;
    }

    public abstract void usePower(Player p);
}
