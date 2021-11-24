package fr.unice.polytech.startingpoint.characters;
import fr.unice.polytech.startingpoint.Player;


public abstract class Character {
    protected int order;
    protected boolean available;
    protected String name;
    protected Player player;

    public int getOrder() {return order;}
    public boolean isAvailable(){return available;}
    public String getName(){return name;}
    public Player getPlayer(){return player;}
    public void isTaken(){available=false;}//un bot prend la carte
    public void setFree(){available=true;}//liberation de la carte
    public void setPlayer(Player p){player =p;}
    public void playerNull(){player =null;}
}
