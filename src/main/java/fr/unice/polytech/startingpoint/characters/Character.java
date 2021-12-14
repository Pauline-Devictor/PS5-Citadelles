package fr.unice.polytech.startingpoint.characters;


import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.buildings.Prestige;
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

    /**
     *
     * @return A reprentation of the Character
     */
    @Override
    public String toString() {
        return character.toString();
    }

    /**
     * Resets the state of every Role
     */
    public void resetRole() {
        //Not killed anymore
        isMurdered = false;
        //Free the character after each turn
        available = true;
        //No thiefplayer anymore
        thief = Optional.empty();
        //not immune to Condottiere anymore

    }

    /**
     * Uses a Character's power
     * @param board the game current's board
     */
    public abstract void usePower(Board board);

    /**
     * Finds the Character's player
     * @param board the game current's board
     * @return the Character's player
     */
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

    /**
     * Collects Character's taxes
     * @param p the Character's player
     * @param d the District's taxes to collect
     */
    public void collectTaxes(Player p, District d) {
        int taxes = 0;
        for (Building b : p.getCity()) {
            if (b.getBuilding().getDistrict() == d) {
                taxes++;
            }
            if (b.getBuilding().equals(BuildingEnum.EcoleDeMagie)) {
                //TODO Implementation des Taxes
                taxes++;
                ((Prestige) b).useEffect(p);
            }
        }
        p.takeMoney(taxes);

        String res;
        if (taxes <= p.getBoard().getBank().getGold())
            res = "Il a récupéré " + taxes + " pieces des quartiers " + d.name();
        else
            res = "La banque n'a plus assez de pieces, il a récupéré " + p.getBoard().getBank().getGold() + " pieces d'or";
        System.out.println(res);
    }

    /**
     *
     * @return ture if the Character's available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Sets the Character's availability
     * @param available
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     *
     * @return true if the Character's murdered
     */
    public boolean isMurdered() {
        return isMurdered;
    }

    /**
     * Sets the Character's state
     * @param murdered
     */
    public void setMurdered(boolean murdered) {
        isMurdered = murdered;
    }

    /**
     *
     * @return The Thief's player
     */
    public Optional<Player> getThief() {
        return thief;
    }

    /**
     * Sets the Thief's player
     * @param thief
     */
    protected void setThief(Optional<Player> thief) {
        this.thief = thief;
    }

    /**
     * @return the Character's name
     */
    public String getName() {
        return character.getName();
    }

    /**
     *
     * @return the Character's order
     */
    public int getOrder() {
        return character.getOrder();
    }

    /**
     *
     * @param p the player
     * @return the effect used
     */
    public String printEffect(Player p) {
        return p.getName() + " a utilisé l'effet de : " + getName() + ".";
    }
}
