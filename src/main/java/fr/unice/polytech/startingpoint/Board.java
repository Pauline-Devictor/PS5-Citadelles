package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.*;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Deck pile;
    private final Bank bank;
    private final List<Character> characters;

    Board() {
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
    }

    //Libere tous les roles et vide la liste des roles pris
    void setAllFree() {
        characters.forEach(Character::resetRole);
    }

    public Deck getPile() {
        return pile;
    }

    public Bank getBank() {
        return bank;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public Character getCharactersInfos(int index) {
        return characters.get(index);
    }

    /*String showBoard(List<Player> players){
        StringBuilder res = new StringBuilder();
        for (Player player : players) {
            res.append("Joueur ").append(player.getName()).append(" :\n").append(player).append("\n");
        }
        return String.valueOf(res);
    }*/
}
