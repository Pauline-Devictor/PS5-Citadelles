package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.buildings.Manufactory;

import static java.util.Objects.isNull;

public class HighThiefManufactory extends HighScoreThief {


    /**
     * Instantiates a new High score thief.
     *
     * @param b the Board
     */
    public HighThiefManufactory(Board b) {
        super(b);
    }

    public HighThiefManufactory(Board b, String name) {
        super(b, name);
    }

    /**
     * Try to pick Manufactory, then Noble, or the bigger cost
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
        else if (b1 instanceof Manufactory)
            return -1;
        else if (b2 instanceof Manufactory)
            return 1;
        else if (b1.getCost() <= costMax && b1.getCost() >= costMin && b2.getCost() <= costMax && b2.getCost() >= costMin) {
            if (b1.getDistrict() == District.Noble && b2.getDistrict() == District.Noble)
                return (b2.getCost() - b1.getCost());
            if (b1.getDistrict() == District.Noble) return -1;
            if (b2.getDistrict() == District.Noble) return 1;
        }
        return (b2.getCost() - b1.getCost());
    }
}
