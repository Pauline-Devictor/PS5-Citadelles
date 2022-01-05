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

        //current player has 7 builds
        if(this.getCity().size() == 7){
            //to kill the Condottiere
            if (pickRole(CharacterEnum.Assassin.getOrder())) {
                ((Assassin) board.getCharacters().get(CharacterEnum.Assassin.getOrder()))
                        .setPriorityTarget(CharacterEnum.Condottiere.getOrder());
                return;
            }
            else if (pickRole(CharacterEnum.Bishop.getOrder())) return;
            else if (pickRole(CharacterEnum.Condottiere.getOrder())) return;
        }

        //test special cases
        boolean enemyHas7builds = false;
        boolean enemyArchi = false;
        boolean enemyHas6builds = false;

        for (Player p : board.getPlayers()){
            if(p.hashCode() == this.hashCode()){
                continue;
            }
            if(p.getCity().size() == 7){
                enemyHas7builds = true;
                continue;
            }
            if(p.getCity().size() == 6){
               enemyHas6builds = true;
               continue;
            }
            if(p.getGold() >= 4 && p.getCardHand().size() >= 1 && p.getCity().size() >= 5) {
                enemyArchi = true;
            }
        }

        //case: a 7-build enemy
        if(enemyHas7builds){
            //if Bishop and Condottiere available, pick condottiere
           if(board.getCharactersInfos(CharacterEnum.Bishop.getOrder()-1).isAvailable()
              && board.getCharactersInfos(CharacterEnum.Condottiere.getOrder()-1).isAvailable()){
               pickRole(CharacterEnum.Condottiere.getOrder()-1);
           if(board.getCharactersInfos(CharacterEnum.Bishop.getOrder()).isAvailable()
              && board.getCharactersInfos(CharacterEnum.Condottiere.getOrder()).isAvailable()){
               pickRole(CharacterEnum.Condottiere.getOrder());
               return;
           }
           //if Assassin and Condottiere available, pick Assassin
           else if(board.getCharactersInfos(CharacterEnum.Assassin.getOrder()-1).isAvailable()
                   && board.getCharactersInfos(CharacterEnum.Condottiere.getOrder()-1).isAvailable()){
               pickRole(CharacterEnum.Assassin.getOrder()-1);
           else if(board.getCharactersInfos(CharacterEnum.Assassin.getOrder()).isAvailable()
                   && board.getCharactersInfos(CharacterEnum.Condottiere.getOrder()).isAvailable()){
               pickRole(CharacterEnum.Assassin.getOrder());
               return;
           }
           //if Assassin and Bishop available, pick Assassin
           else if(board.getCharactersInfos(CharacterEnum.Assassin.getOrder()).isAvailable()
                   && board.getCharactersInfos(CharacterEnum.Bishop.getOrder()).isAvailable()){
               pickRole(CharacterEnum.Assassin.getOrder());
               for (Player p : board.getPlayers()) {
                   //if the 7-builds player has an empty hand, kill Magician
                   if(p.getCity().size() == 7 && p.getCardHand().isEmpty()){
                       ((Assassin) board.getCharacters().get(CharacterEnum.Assassin.getOrder()))
                               .setPriorityTarget(CharacterEnum.Magician.getOrder());
                       return;
                   }
               }
               //else, kill Bishop
               ((Assassin) board.getCharacters().get(CharacterEnum.Assassin.getOrder()))
                       .setPriorityTarget(CharacterEnum.Bishop.getOrder());
               return;
           }
           //if 2nd / 3rd to pick, pick what is left
            if (pickRole(CharacterEnum.Assassin.getOrder()-1)) return;
            if(pickRole(CharacterEnum.Condottiere.getOrder()-1)) return;
            if(pickRole(CharacterEnum.Bishop.getOrder()-1)) return;
        }

        //case: someone has too much advance (might finish with Archi)
        else if (enemyArchi) {
            if (pickRole(CharacterEnum.Assassin.getOrder())) {
                //to kill Architect
                ((Assassin) board.getCharacters().get(CharacterEnum.Assassin.getOrder()))
                        .setPriorityTarget(CharacterEnum.Architect.getOrder());
                return;
            }
            else if (pickRole(CharacterEnum.Architect.getOrder())) return;
        }

        //case: a 6-build enemy
        else if (enemyHas6builds) {
            //to prevent him from taking King -> Assassin
            if (pickRole(CharacterEnum.King.getOrder())) return;
            //to kill the King
            else if(pickRole(CharacterEnum.Assassin.getOrder())) {

                ((Assassin) board.getCharacters().get(CharacterEnum.Assassin.getOrder()))
                        .setPriorityTarget(CharacterEnum.King.getOrder());
                return;
            }
            //to destroy a building from the player
            else if(pickRole(CharacterEnum.Condottiere.getOrder())) return;
            //to prevent him from playing Bishop and have no Building destroyed
            else if (pickRole(CharacterEnum.Bishop.getOrder())) return;
        }


        //initialize with main default choices
        ArrayList<Integer> characters = new ArrayList<>(List.of(
                CharacterEnum.Bishop.getOrder(),
                CharacterEnum.Condottiere.getOrder(),
                CharacterEnum.Thief.getOrder()
        ));

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
