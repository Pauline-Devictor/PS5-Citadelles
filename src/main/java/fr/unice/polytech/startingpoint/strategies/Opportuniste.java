package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.Assassin;
import fr.unice.polytech.startingpoint.characters.CharacterEnum;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class Opportuniste extends Player {

    public Opportuniste(Board b) { super(b, "Opportuniste");}

    /**
     * Choose the Role depending on the state of the game :
     * Main characters: Bishop, Condottiere, Thief
     * Then adapts when a player is about to win
     */
    @Override
    public void chooseRole(){
        //initialize with main default choices
        ArrayList<Integer> characters = new ArrayList<>(List.of(
                CharacterEnum.Bishop.getOrder() - 1,
                CharacterEnum.Condottiere.getOrder() - 1,
                CharacterEnum.Thief.getOrder() - 1
        ));

        //condition to not let someone play Architect
        for (Player p : board.getPlayers()) {
            if (this.hashCode() != p.hashCode() && p.getGold() >= 4 && p.getCardHand().size() >= 1 && p.getCity().size() >= 5) {
                if (board.getCharactersInfos(CharacterEnum.Assassin.getOrder() - 1).isAvailable()) {
                    //to kill Architect
                    ((Assassin) board.getCharacters().get(CharacterEnum.Assassin.getOrder() - 1)).setPriorityTarget(CharacterEnum.Architect.getOrder());
                    characters.add(0, CharacterEnum.Assassin.getOrder() - 1);
                }
                else if (board.getCharactersInfos(CharacterEnum.Architect.getOrder() - 1).isAvailable())
                    //to prevent player from playing Architect
                    characters.add(0, CharacterEnum.Architect.getOrder() - 1);
            }
        }

        //case: a 6-buildind player
        for (Player p : board.getPlayers()) {
            if (this.hashCode() != p.hashCode() && p.getCity().size() == 6) {
                if (board.getCharactersInfos(CharacterEnum.King.getOrder() - 1).isAvailable())
                    //to prevent him from taking King -> Assassin
                    characters.add(0, CharacterEnum.King.getOrder() - 1);
                else if(board.getCharactersInfos(CharacterEnum.Assassin.getOrder() - 1).isAvailable()) {
                    //to kill the King
                    ((Assassin) board.getCharacters().get(CharacterEnum.Assassin.getOrder() - 1)).setPriorityTarget(CharacterEnum.King.getOrder());
                    characters.add(0, CharacterEnum.Assassin.getOrder() - 1);
                }
                else if(board.getCharactersInfos(CharacterEnum.Condottiere.getOrder() - 1).isAvailable())
                    //to destroy a building from the player
                    characters.add(0, CharacterEnum.Condottiere.getOrder() - 1);
                else if (board.getCharactersInfos(CharacterEnum.Bishop.getOrder() - 1).isAvailable())
                    //to prevent him from playing Bishop and have no Building destroyed
                    characters.add(0, CharacterEnum.Bishop.getOrder() - 1);
            }
        }

        //adds all missing characters in order
            for (int i = 0; i < 8; i++) {
                if (!characters.contains(i)) characters.add(i);
            }

            for (int elem : characters) {
                if (pickRole(elem)) {
                    return;
                }
            }
        }

    /**
     * @param d the Buildding's District
     * @return the weight of the District
     */
    private int calculDistrict(District d) {
        return switch (d) {
            case Religion -> 2;
            case Military -> 1;
            default -> 0;
        };
    }

    /**
     * Try to pick Religion, then Military, then the higher cost
     *
     * @param b1 The First Building
     * @param b2 The Second Building
     * @return negative for b1, positive for b2, zero otherwise
     */
    @Override
    public int compare(Building b1, Building b2) {
        if (isNull(b1))
            return 1;
        else if (isNull(b2))
            return -1;
        else if (getCardHand().contains(b1))
            return 1;
        else if (getCardHand().contains(b2))
            return -1;
        //Quartier prioritaire, si egalité cout du quartier
        int score1 = calculDistrict(b1.getDistrict()) * 10 + b1.getCost();
        int score2 = calculDistrict(b2.getDistrict()) * 10 + b2.getCost();
        return score2 - score1;
    }
}
