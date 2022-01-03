package fr.unice.polytech.startingpoint.characters;


import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.buildings.Prestige;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * The type Character.
 */
public abstract class Character {
    /**
     * The Character.
     */
    protected final CharacterEnum character;
    /**
     * The Available.
     */
    protected boolean available;
    private boolean isMurdered;
    private boolean stolen;
    //empty si le personnage n'est pas volé, le voleur sinon
    private Player thief;

    /**
     * Instantiates a new Character.
     *
     * @param character the character
     */
    public Character(CharacterEnum character) {
        this.character = character;
        available = true;
        isMurdered = false;
        stolen = false;
        thief = null;
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
     * Resets the state of the character
     */
    public void resetRole() {
        //Not killed anymore
        isMurdered = false;
        //Free the character after each turn
        available = true;
        //No thiefplayer anymore
        thief = null;
        //not immune to Condottiere anymore
        stolen = false;
    }

    /**
     * Uses a Character's power
     *
     * @param board the game current's board
     */
    public abstract void usePower(Board board);

    /**
     * Finds the Character's player
     *
     * @param board the game current's board
     * @return the Character's player
     */
    public Optional<Player> findPlayer(Board board) {
        Optional<Player> p = Optional.empty();
        for (Player player : board.getPlayers()) {
            if (!isNull(player.getRole())) {
                if (player.getRole().getClass().equals(getClass()))
                    p = Optional.of(player);
            }
        }
        return p;
    }

    /**
     * Collects Character's taxes
     * Collect 1 gold per District build by the player
     *
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
                taxes++;
                ((Prestige) b).useEffect(p);
            }
        }
        p.takeMoney(taxes);
        p.getBoard().showTaxes(d, p, taxes);
    }

    /**
     * Is available boolean.
     *
     * @return true if the Character's available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Is murdered boolean.
     *
     * @return true if the Character's murdered
     */
    public boolean isMurdered() {
        return isMurdered;
    }

    /**
     * Kill.
     */
    public void kill() {
        isMurdered = true;
    }

    /**
     * Gets thief.
     *
     * @return The Thief's player
     */
    public Player getThief() {
        return thief;
    }

    /**
     * Stole by.
     *
     * @param thief the thief
     */
    protected void stoleBy(Player thief) {
        this.thief = thief;
        stolen = true;
    }

    /**
     * Gets name.
     *
     * @return the Character's name
     */
    public String getName() {
        return character.getName();
    }

    /**
     * Gets order.
     *
     * @return the Character's order
     */
    public int getOrder() {
        return character.getOrder();
    }

    /**
     * Print effect.
     *
     * @param p the player
     */
    public void printEffect(Player p) {
        p.getBoard().showCharacterEffect(p);
    }

    /**
     * Map list and sort by city size.
     *
     * @param board the board
     * @return the list
     */
    protected List<Player> map(Board board) {
        TreeMap<Integer, Player> cityMap = new TreeMap<>();
        for (Player p : board.getPlayers()) {
            cityMap.put(p.getCity().size(), p);
        }
        return cityMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    /**
     * Took.
     */
    public void took() {
        available = false;
    }

    /**
     * Is stolen boolean.
     *
     * @return the boolean
     */
    public boolean isStolen() {
        return stolen;
    }
}
