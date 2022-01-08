package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.CharacterEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
        super(b);
    }

    public HighScoreThief(Board b, String name) {
        super(b, name);
    }

    /**
     * Try to pick Thief, if not try the king
     */
    @Override
    public void chooseRole() {
        TreeMap<District, Integer> taxmap = getDistrictValues();
        taxmap.remove(District.Prestige);
        taxmap.remove(District.Noble);
        ArrayList<Integer> taxList = mapToSortedList(taxmap);

        taxList.add(0, CharacterEnum.King.getOrder());
        taxList.add(0, CharacterEnum.Thief.getOrder());
        taxList.addAll(List.of(
                CharacterEnum.Assassin.getOrder(),
                CharacterEnum.Magician.getOrder(),
                CharacterEnum.Architect.getOrder()
        ));

        pickRole(taxList);
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
        else //noinspection DuplicatedCode
            if (b1.getCost() <= costMax && b1.getCost() >= costMin && b2.getCost() <= costMax && b2.getCost() >= costMin) {
                if (b1.getDistrict() == District.Noble && b2.getDistrict() == District.Noble)
                    return (b2.getCost() - b1.getCost());
                if (b1.getDistrict() == District.Noble) return -1;
                if (b2.getDistrict() == District.Noble) return 1;
            }
        return (b2.getCost() - b1.getCost());
    }
}
