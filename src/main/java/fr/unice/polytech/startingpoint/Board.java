package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.*;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Deck pile;
    private final Bank bank;
    private final List<Character> characters = new ArrayList<>();

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

    Board() {
        this.bank = new Bank(30);
        this.pile = new Deck();
        characters.add(new Assassin());
        characters.add(new Thief());
        characters.add(new Magician());
        characters.add(new King());
        characters.add(new Bishop());
        characters.add(new Merchant());
        characters.add(new Architect());
        characters.add(new Condottiere());
    }

    //Libere tous les roles et vide la liste des roles pris
    void setAllFree() {
        for (Character character : characters) {
            character.setFree();
            character.playerNull();
        }
    }

    /*String showBoard(List<Player> players){
        StringBuilder res = new StringBuilder();
        for (Player player : players) {
            res.append("Joueur ").append(player.getName()).append(" :\n").append(player).append("\n");
        }
        return String.valueOf(res);
    }*/
}
