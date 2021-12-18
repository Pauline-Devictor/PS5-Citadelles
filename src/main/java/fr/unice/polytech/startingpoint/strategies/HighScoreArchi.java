package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.CharacterEnum;

import java.util.*;
import java.util.stream.Collectors;

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
        super(b, "HautScoreArchitect");
    }

    /**
     * Choose the Role depending of the state of the game :
     * Architect if he's rich, Marchand otherwise
     * Condottiere if someone is close to finish
     */
    @Override
    public void chooseRole() {
        //Taxes priority
        TreeMap<District, Integer> taxmap = new TreeMap<>();
        for (District d : District.values()) {
            taxmap.put(d, 0);
        }
        for (Building b : getCity()) {
            taxmap.put(b.getDistrict(), taxmap.get(b.getDistrict()) + 1);
        }
        taxmap.remove(District.Prestige);
        taxmap.remove(District.Military);
        ArrayList<Integer> taxList = (ArrayList<Integer>) taxmap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .map(District::getTaxCollector)
                .collect(Collectors.toList());

        Collections.reverse(taxList);

        //if rich, architect
        if (getGold() > 10) taxList.add(0, CharacterEnum.Architect.getOrder() - 1);
        else taxList.add(CharacterEnum.Merchant.getOrder() - 1);

        //if a player has too much advance, condottiere
        Player biggestCity = board.getPlayers().get(0);
        for (Player p : board.getPlayers()) {
            if (p.getCity().size() > biggestCity.getCity().size()) biggestCity = p;
        }
        if ((biggestCity.getCity().size() - city.size() > 5)) taxList.add(0, CharacterEnum.Condottiere.getOrder() - 1);
        else taxList.add(CharacterEnum.Condottiere.getOrder() - 1);

        taxList.addAll(List.of(
                CharacterEnum.Magician.getOrder() - 1,
                CharacterEnum.Assassin.getOrder() - 1,
                CharacterEnum.Thief.getOrder() - 1
        ));

        for (int elem : taxList) {
            if (pickRole(elem)) {
                return;
            }
        }
    }

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
