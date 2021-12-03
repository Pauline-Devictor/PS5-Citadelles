package fr.unice.polytech.startingpoint.characters;
import fr.unice.polytech.startingpoint.Player;


public abstract class Character {
    private final int order;
    protected boolean available;
    private final String name;
    protected Player player;
    private boolean isMurdered=false;

    public int getOrder() {return order;}
    public boolean isAvailable(){return available;}
    public String getName(){return name;}
    public Player getPlayer(){return player;}
    public void isTaken(){available=false;}//un bot prend la carte
    public void setPlayer(Player p){player =p;}

    @Override
    public String toString() {
        return name;
    }
    public Character(int order, String name) {
        this.order=order;
        this.name=name;
        this.available =true;

    }

    public void resetRole(){
        //No player anymore
        player =null;
        //Not killed anymore
        isMurdered=false;
        //Free the character after each turn
        available=true;
    }
    public void setMurdered(){
        isMurdered=true;
    }
    public boolean gotMurdered(){
        return isMurdered;
    }
    public abstract void usePower();
}
