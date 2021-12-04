package fr.unice.polytech.startingpoint.characters;
import fr.unice.polytech.startingpoint.Player;


public abstract class Character {
    private final int order;
    protected boolean available;
    private final String name;
    private boolean isMurdered;
    private boolean isStolen;
    protected Player player;

    public Character(int order, String name) {
        this.order = order;
        this.name = name;
        available = true;
        isMurdered = false;
        isStolen = false;

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
        //Not stolen anymore
        isStolen = false;
        //No thiefplayer anymore
    }

    public void setMurdered() {
        isMurdered = true;
    }

    public boolean gotMurdered() {
        return isMurdered;
    }

    public void setStolen(){isStolen = true;}

    public boolean gotStolen(){return isStolen;}

    public void setPlayer(Player p){player = p;}

    public Player getPlayer(){return player;}

    public abstract void usePower(Player p);
}
