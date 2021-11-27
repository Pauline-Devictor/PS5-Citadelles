package fr.unice.polytech.startingpoint.characters;
import fr.unice.polytech.startingpoint.Player;


public abstract class Character {
    private int order;
    protected boolean available;
    private String name;
    protected Player player;
    private boolean isMurdered=false;

    public Character(int order, String name) {
        this.order=order;
        this.name=name;
        this.available =true;
    }

    public int getOrder() {return this.order;}
    public boolean isAvailable(){return this.available;}
    public String getName(){return this.name;}
    public Player getPlayer(){return this.player;}
    public void isTaken(){this.available=false;}//un bot prend la carte
    public void setPlayer(Player p){this.player =p;}
    public void resetRole(){
        //No player anymore
        this.player =null;
        //Not killed anymore
        this.isMurdered=false;
        //Free the character after each turn
        this.available=true;
    }
    public void setMurdered(){
        this.isMurdered=true;
    }
    public boolean gotMurdered(){
        return this.isMurdered;
    }
    public abstract void usePower();
}
