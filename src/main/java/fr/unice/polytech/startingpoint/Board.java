package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.strategies.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Board.
 */
public class Board {

    private final Deck pile;
    private final Bank bank;
    private final List<Character> characters;
    private final List<Player> players;

    public Board(String... players) {
        this.bank = new Bank(30);
        this.pile = new Deck();
        characters = List.of(new Assassin(),
                new Thief(),
                new Magician(),
                new King(),
                new Bishop(),
                new Merchant(),
                new Architect(),
                new Condottiere());
        List<Player> tmp = new ArrayList<>();
        if (players.length > 6 || players.length < 2) {
            tmp.add(new HighScoreArchi(this));
            tmp.add(new HighScoreThief(this));
            tmp.add(new HighThiefManufactory(this));
            tmp.add(new RushArchi(this));
        } else {
            for (int i = 0; i < players.length; i++) {
                switch (players[i]) {
                    case "RushMerch" -> tmp.add(new RushMerch(this, String.valueOf(i)));
                    case "HighScoreArchi" -> tmp.add(new HighScoreArchi(this, String.valueOf(i)));
                    case "HighScoreThief" -> tmp.add(new HighScoreThief(this, String.valueOf(i)));
                    case "HighThiefManufacture" -> tmp.add(new HighThiefManufactory(this, String.valueOf(i)));
                    case "Opportuniste" -> tmp.add(new Opportuniste(this, String.valueOf(i)));
                    case "RushArchi" -> tmp.add(new RushArchi(this, String.valueOf(i)));
                    case "RushArchiLab" -> tmp.add(new RushArchiLab(this, String.valueOf(i)));
                    default -> tmp.add(new Player(this, String.valueOf(i)));
                }
            }
        }
        this.players = tmp;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Free every role and empty the list of the taken roles
     */
    void release() {
        characters.forEach(Character::resetRole);
        players.forEach(Player::reset);
    }

    /**
     * Gets pile.
     *
     * @return the pile
     */
    public Deck getPile() {
        return pile;
    }

    /**
     * Gets bank.
     *
     * @return the bank
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * @return List of every role
     */
    public List<Character> getCharacters() {
        return characters;
    }

    /**
     * Gets characters infos.
     *
     * @param index the index
     * @return the characters infos
     */
    public Character getCharactersInfos(int index) {
        if (index < 0)
            index = 0;
        else if (index > characters.size())
            index = characters.size() - 1;
        return getCharacters().get(index);
    } //TODO Find a better way

    /**
     * Put the card b in the deck.
     *
     * @param b The Building
     */
    public void putCard(Building b) {
        pile.putCard(b);
    }

}