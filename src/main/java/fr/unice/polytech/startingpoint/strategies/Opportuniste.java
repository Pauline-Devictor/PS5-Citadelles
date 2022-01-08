package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.Assassin;
import fr.unice.polytech.startingpoint.characters.CharacterEnum;

import java.util.ArrayList;
import java.util.List;

import static fr.unice.polytech.startingpoint.characters.CharacterEnum.Bishop;
import static java.util.Objects.isNull;

public class Opportuniste extends Player {

    public Opportuniste(Board b) {
        super(b);
    }

    public Opportuniste(Board b, String name) {
        super(b, name);
    }


    /**
     * Choose the Role depending on the state of the game :
     * Main characters: Bishop, Condottiere, Thief
     * Then adapts when a player is about to win
     */
    @Override
    public void chooseRole() {

        //current player has 7 builds
        if (getCity().size() > 6) {
            //to kill the Condottiere
            if (pickRole(CharacterEnum.Assassin.getOrder())) {
                ((Assassin) board.getCharacters().get(CharacterEnum.Assassin.getOrder()))
                        .setPriorityTarget(CharacterEnum.Condottiere.getOrder());
                return;
            } else if (pickRole(Bishop.getOrder())) return;
            else if (pickRole(CharacterEnum.Condottiere.getOrder())) return;
        }

        //test special cases
        boolean enemyHas7builds = false;
        boolean enemyArchi = false;
        boolean enemyHas6builds = false;

        for (Player p : board.getPlayers()) {
            if (p.hashCode() != this.hashCode()) {
                if (p.getCity().size() == 7) {
                    enemyHas7builds = true;
                    break;
                } else if (p.getCity().size() == 6) {
                    enemyHas6builds = true;
                } else if (p.getGold() >= 4 && p.getCardHand().size() >= 1 && p.getCity().size() >= 5) {
                    enemyArchi = true;
                }
            }
        }

        //case: a 7-build enemy
        if (enemyHas7builds) {
            if (sevenBuildsOpponent()) return;
        }

        //case: someone has too much advance (might finish with Archi)
        else if (enemyArchi) {
            if (pickRole(CharacterEnum.Assassin.getOrder())) {
                //try to kill Architect
                ((Assassin) board.getCharacters().get(CharacterEnum.Assassin.getOrder()))
                        .setPriorityTarget(CharacterEnum.Architect.getOrder());
                return;
                //otherwise, try to pick Assassin
            } else if (pickRole(CharacterEnum.Architect.getOrder())) return;
        }

        //case: a 6-build enemy
        else if (enemyHas6builds) {
            if (sixBuildsOpponent()) return;
        }

        //initialize with main default choices
        ArrayList<Integer> characters = new ArrayList<>(List.of(
                Bishop.getOrder(),
                CharacterEnum.Condottiere.getOrder(),
                CharacterEnum.Thief.getOrder()
        ));
        //adds all missing characters in order
        for (int i = 0; i < 8; i++) {
            if (!characters.contains(i)) characters.add(i);
        }
        pickRole(characters);
    }

    private boolean sevenBuildsOpponent() {
        CharacterEnum choice = null;
        //if Bishop and Condottiere available, pick condottiere
        boolean bishopAvailable = characterAvailable(Bishop);
        boolean assassinAvailable = characterAvailable(CharacterEnum.Assassin);
        boolean condottiereAvailable = characterAvailable(CharacterEnum.Condottiere);
        if (bishopAvailable && condottiereAvailable)
            choice = CharacterEnum.Condottiere;
            //if Assassin and Condottiere available, pick Assassin
        else if (assassinAvailable && condottiereAvailable)
            choice = CharacterEnum.Assassin;
            //if Assassin and Bishop available, pick Assassin
        else if (assassinAvailable && bishopAvailable) {
            choice = CharacterEnum.Assassin;
            priorityTarget7Builds();
        }

        List<Integer> res = new ArrayList<>(List.of(CharacterEnum.Assassin.getOrder(), CharacterEnum.Condottiere.getOrder(), Bishop.getOrder()));
        if (!isNull(choice))
            res.add(0, choice.getOrder());
        return pickRole(res);
        //if 2nd / 3rd to pick, pick what is left
    }

    private boolean characterAvailable(CharacterEnum c) {
        return getBoard().getCharactersInfos(c.getOrder()).isAvailable();
    }

    private void priorityTarget7Builds() {
        CharacterEnum choice = null;
        for (Player p : board.getPlayers()) {
            //if the 7-builds player has an empty hand, kill Magician
            if (p.getCity().size() == 7 && p.getCardHand().isEmpty()) {
                choice = CharacterEnum.Magician;
                break;
            }
        }//else, kill Bishop
        ((Assassin) board.getCharacters().get(CharacterEnum.Assassin.getOrder())).setPriorityTarget((isNull(choice) ? Bishop : choice).getOrder());
    }


    private boolean sixBuildsOpponent() {
        CharacterEnum choice = null;
        //to prevent him from taking King -> Assassin
        if (characterAvailable(CharacterEnum.King))
            choice = CharacterEnum.King;
            //to kill the King
        else if (characterAvailable(CharacterEnum.Assassin)) {
            choice = CharacterEnum.Assassin;
            ((Assassin) board.getCharacters().get(CharacterEnum.Assassin.getOrder()))
                    .setPriorityTarget(CharacterEnum.King.getOrder());
        }
        List<Integer> res = new ArrayList<>(List.of(CharacterEnum.Condottiere.getOrder(), CharacterEnum.Bishop.getOrder()));
        if (!isNull(choice))
            res.add(0, choice.getOrder());
        return pickRole(res);
        //First choice to destroy a building from the player
        //Last one to prevent him from playing Bishop and have no Building destroyed
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
