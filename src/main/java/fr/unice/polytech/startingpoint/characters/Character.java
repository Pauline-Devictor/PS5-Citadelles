package fr.unice.polytech.startingpoint.characters;


import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.District;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;

public abstract class Character {
    private final int order;
    protected boolean available;
    private final String name;
    private boolean isMurdered;
    //empty si le personnage n'est pas volé, le voleur sinon
    private Optional<Player> thief;

    public Character(int order, String name) {
        this.order = order;
        this.name = name;
        available = true;
        isMurdered = false;
        thief = Optional.empty();

    }

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
        thief = Optional.empty();
        //No thiefplayer anymore
    }

    public abstract void usePower(Board board);

    public Optional<Player> findPlayer(Board board) {
        Optional<Player> p = Optional.empty();
        for (Player player : board.getPlayers()) {
            if (player.getRole().get().getClass().equals(getClass()))
                p = Optional.of(player);
        }
        return p;
    }

    public void collectTaxes(Player p, District d) {
        int taxes = 0;
        for (Building b : p.getCardHand()) {
            if (b.getBuilding().getDistrict() == d) {
                taxes++;
            }
            p.setTaxes(taxes);
        }
    }

    public int getOrder() {
        return order;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public boolean isMurdered() {
        return isMurdered;
    }

    public void setMurdered(boolean murdered) {
        isMurdered = murdered;
    }

    public Optional<Player> getThief() {
        return thief;
    }

    public void setThief(Optional<Player> thief) {
        this.thief = thief;
    }
}
