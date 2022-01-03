package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.CharacterEnum;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * The type High score thief.
 */
public class HighScoreThief extends Player {
    protected final int costMax = 6;
    protected final int costMin = 3;

    /**
     * Instantiates a new High score thief.
     *
     * @param b the Board
     */
    public HighScoreThief(Board b) {
        super(b, "HautScoreVoleur");
    }

    public HighScoreThief(Board b, String name) {
        super(b, name);
    }

    /**
     * Try to pick Thief, if not try the king
     */
    @Override
    public void chooseRole() {
        TreeMap<District, Integer> taxmap = new TreeMap<>();
        for (District d : District.values()) {
            taxmap.put(d, 0);
        }
        for (Building b : getCity()) {
            taxmap.put(b.getDistrict(), taxmap.get(b.getDistrict()) + 1);
        }
        taxmap.remove(District.Prestige);
        taxmap.remove(District.Noble);
        ArrayList<Integer> taxList = (ArrayList<Integer>) taxmap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .map(District::getTaxCollector)
                .collect(Collectors.toList());

        Collections.reverse(taxList);

        taxList.add(0, CharacterEnum.King.getOrder() - 1);
        taxList.add(0, CharacterEnum.Thief.getOrder() - 1);
        taxList.addAll(List.of(
                CharacterEnum.Assassin.getOrder() - 1,
                CharacterEnum.Magician.getOrder() - 1,
                CharacterEnum.Architect.getOrder() - 1
        ));

        for (int elem : taxList) {
            if (pickRole(elem)) {
                return;
            }
        }
    }

    /**
     * Try to pick Noble, or the bigger cost
     *
     * @param b1 The First Building
     * @param b2 The Second Building
     * @return negative for b1, positive for b2, zero otherwise
     */
    @Override
    public int compare(Building b1, Building b2) {
        //return -1 pour b1, 1 pour b2
        if (isNull(b1))
            return 1;
        else if (isNull(b2))
            return -1;
        else if (getCardHand().contains(b1))
            return 1;
        else if (getCardHand().contains(b2))
            return -1;
        else if (b1.getCost() <= costMax && b1.getCost() >= costMin && b2.getCost() <= costMax && b2.getCost() >= costMin) {
            if (b1.getDistrict() == District.Noble && b2.getDistrict() == District.Noble) return (b2.getCost() - b1.getCost());
            if (b1.getDistrict() == District.Noble) return -1;
            if (b2.getDistrict() == District.Noble) return 1;
        }
        return (b2.getCost() - b1.getCost());
    }
}
