package fr.unice.polytech.startingpoint.characters;


import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;

public abstract class Character {
    protected final CharacterEnum character;
    protected boolean available;
    private boolean isMurdered;
    //empty si le personnage n'est pas volé, le voleur sinon
    private Optional<Player> thief;

    public Character(CharacterEnum character) {
        this.character = character;
        available = true;
        isMurdered = false;
        thief = Optional.empty();
    }

    @Override
    public String toString() {
        return character.toString();
    }

    public void resetRole() {
        //Not killed anymore
        isMurdered = false;
        //Free the character after each turn
        available = true;
        //No thiefplayer anymore
        thief = Optional.empty();
        //not immune to Condottiere anymore

    }

    public abstract void usePower(Board board);

    public Optional<Player> findPlayer(Board board) {
        Optional<Player> p = Optional.empty();
        for (Player player : board.getPlayers()) {
            if (player.getRole().isPresent()) {
                if (player.getRole().get().getClass().equals(getClass()))
                    p = Optional.of(player);
            }
        }
        return p;
    }

    public void collectTaxes(Player p, District d) {
        int taxes = 0;
        for (Building b : p.getCity()) {
            if (b.getBuilding().getDistrict() == d) {
                taxes++;
            }
            p.setTaxes(taxes);
        }
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
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

    protected void setThief(Optional<Player> thief) {
        this.thief = thief;
    }

    public String getName() {
        return character.getName();
    }

    public int getOrder() {
        return character.getOrder();
    }
}
