package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.Characters.*;
import fr.unice.polytech.startingpoint.Characters.Character;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Deck pile;
    private final Bank bank;
    private final List<Character> characters = new ArrayList<>();
    private List<Character> role_taken = new ArrayList<>();

    public Deck getPile() {
        return pile;
    }

    public Bank getBank() {
        return bank;
    }

    public Character getCharactersInfos(int index){return characters.get(index);}

    public List<Character> getRole_taken(){return role_taken;}

    Board(){
        this.bank = new Bank(30);
        this.pile = new Deck();
        this.characters.add(new Assassin());
        this.characters.add(new Thief());
        this.characters.add(new Magician());
        this.characters.add(new King());
        this.characters.add(new Bishop());
        this.characters.add(new Merchant());
        this.characters.add(new Architect());
        this.characters.add(new Condottiere());
    }

    //Libere tous les roles et vide la liste des roles pris
    void setAllFree(){
        for (Character character : characters) {
            character.setFree();
            role_taken = new ArrayList<>();
        }
    }

    //actualise la liste des roles pris
    void playerTake(int index){
        role_taken.add(characters.get(index));
    }

    String showBoard(List<Player> players){
        int nb_players = players.size();
        StringBuilder res = new StringBuilder();
        for (Player player : players) {
            res.append("Joueur ").append(player.getName()).append(" :\n").append(player).append("\n");
        }
        return String.valueOf(res);
    }
}
