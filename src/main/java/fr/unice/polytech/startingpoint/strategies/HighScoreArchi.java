package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.CharacterEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static fr.unice.polytech.startingpoint.characters.CharacterEnum.*;
import static java.util.Objects.isNull;

/**
 * The type High score archi.
 */
public class HighScoreArchi extends Player {
    private final int costMax = 6;
    private final int costMin = 3;

    /**
     * Instantiates a new High score archi.
     *
     * @param b the b
     */
    public HighScoreArchi(Board b) {
        super(b);
    }

    public HighScoreArchi(Board b, String name) {
        super(b, name);
    }

    /**
     * Choose the Role depending on the state of the game :
     * Architect if he's rich, Marchand otherwise
     * Condottiere if someone is close to finish
     */
    @Override
    public void chooseRole() {
        TreeMap<District, Integer> taxmap = getDistrictValues();
        //Definir l'ordre des taxes
        taxmap.remove(District.Prestige);
        taxmap.remove(District.Military);
        //Retirer les Quartiers Militaire et Prestige

        ArrayList<Integer> taxList = mapToSortedList(taxmap);
        //if rich, architect
        if (getGold() > 10)
            taxList.add(0, Architect.getOrder());
        else
            taxList.add(Merchant.getOrder());

        //if a player has too much advance, condottiere
        if (board.getPlayers().stream().anyMatch(e -> e.getCity().size() - city.size() > 5))
            taxList.add(0, Condottiere.getOrder());
        else
            taxList.add(Condottiere.getOrder());

        taxList.addAll(List.of(
                CharacterEnum.Magician.getOrder(),
                CharacterEnum.Assassin.getOrder(),
                CharacterEnum.Thief.getOrder()
        ));

        pickRole(taxList);
    }

    /**
     * @param d the Buildding's District
     * @return the weight of the District
     */
    private int calculDistrict(District d) {
        return switch (d) {
            case Commercial -> 3;
            case Noble -> 2;
            case Religion -> 1;
            default -> 0;
        };
    }

    /**
     * Try to pick Commercial, then Noble, then Religion, then the higher cost
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
            //S'ils ont un prix acceptable
        else if (b1.getCost() <= costMax && b1.getCost() >= costMin && b2.getCost() <= costMax && b2.getCost() >= costMin) {
            //Quartier prioritaire, si egalité cout du quartier
            int score1 = calculDistrict(b1.getDistrict()) * 10 + b1.getCost();
            int score2 = calculDistrict(b2.getDistrict()) * 10 + b2.getCost();
            return score2 - score1;
        }
        return (b2.getCost() - b1.getCost());
    }
}
