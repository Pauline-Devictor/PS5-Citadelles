package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.CharacterEnum;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * The type Rush merch.
 */
public class RushMerch extends Player {

    /**
     * Instantiates a new Rush merch.
     *
     * @param b the b
     */
    public RushMerch(Board b, String name) {
        super(b, name);
    }

    public RushMerch(Board b) {
        super(b);
    }

    /**
     * Pick in the order of the list : first choice is the merchant, last choice is condottiere
     */
    @Override
    public void chooseRole() {

        //Marchand puis Archi
        ArrayList<Integer> taxList = new ArrayList<>(List.of(
                CharacterEnum.Merchant.getOrder(),
                CharacterEnum.Architect.getOrder(),
                CharacterEnum.Magician.getOrder(),
                CharacterEnum.King.getOrder(),
                CharacterEnum.Thief.getOrder(),
                CharacterEnum.Bishop.getOrder(),
                CharacterEnum.Assassin.getOrder(),
                CharacterEnum.Condottiere.getOrder()
        ));

        for (int elem : taxList) {
            if (pickRole(elem)) {
                return;
            }
        }
    }

    /**
     * Try to pick Commercial, or the minimal cost
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
        else {
            int costMax = 3;
            int costMin = 1;
            int cmp = compareRushDistrict(District.Commercial, b1, b2, costMin, costMax);
            if (cmp != 0)
                return cmp;
        }
        return (b1.getCost() - b2.getCost());
    }
}
