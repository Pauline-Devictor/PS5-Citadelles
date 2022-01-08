package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.CharacterEnum;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * The type Rush archi.
 */
public class RushArchi extends Player {

    /**
     * Instantiates a new Rush archi.
     *
     * @param b the b
     */
    public RushArchi(Board b) {
        super(b);
    }

    public RushArchi(Board b, String name) {
        super(b, name);
    }

    /**
     * Prioritize the Architect, or the Bishop for a big city else Anything
     */
    @Override
    public void chooseRole() {

        //prioritize architect
        ArrayList<Integer> taxList = new ArrayList<>(List.of(
                CharacterEnum.Architect.getOrder(),
                CharacterEnum.Magician.getOrder(),
                CharacterEnum.King.getOrder(),
                CharacterEnum.Merchant.getOrder(),
                CharacterEnum.Thief.getOrder(),
                CharacterEnum.Assassin.getOrder(),
                CharacterEnum.Condottiere.getOrder()
        ));

        //if he has 6+ buildings, Bishop
        if (getCity().size() > 5)
            taxList.add(0, CharacterEnum.Bishop.getOrder());
        else
            taxList.add(CharacterEnum.Bishop.getOrder());


        pickRole(taxList);
    }

    /**
     * Try to pick Religion, or the minimal cost
     *
     * @param b1 The First Building
     * @param b2 The Second Building
     * @return negative for b1, positive for b2, zero otherwise
     */
    @Override
    public int compare(Building b1, Building b2) {
        //return -1 pour b1, 1 pour b2
        int costMin = 1;
        int costMax = 3;
        if (isNull(b1))
            return 1;
        else if (isNull(b2))
            return -1;
        else if (getCardHand().contains(b1))
            return 1;
        else if (getCardHand().contains(b2))
            return -1;
        else {
            int cmp = compareRushDistrict(District.Religion, b1, b2, costMin, costMax);
            if (cmp != 0)
                return cmp;
        }
        return (b1.getCost() - b2.getCost());
    }
}
