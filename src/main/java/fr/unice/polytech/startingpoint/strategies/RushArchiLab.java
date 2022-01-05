package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.buildings.Laboratory;

import static java.util.Objects.isNull;

public class RushArchiLab extends RushArchi {

    /**
     * Instantiates a new Rush archi.
     *
     * @param b the building
     */
    public RushArchiLab(Board b) {
        super(b);
    }

    public RushArchiLab(Board b, String name) {
        super(b, name);
    }

    /**
     * Try to pick a Laboratory, then Religion, or the minimal cost
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
        else if (b1 instanceof Laboratory)
            return -1;
        else if (b2 instanceof Laboratory)
            return 1;
        else if (b1.getCost() <= costMax && b1.getCost() >= costMin && b2.getCost() <= costMax && b2.getCost() >= costMin) {
            if (b1.getDistrict() == District.Religion && b2.getDistrict() == District.Religion)
                return (b1.getCost() - b2.getCost());
            if (b1.getDistrict() == District.Religion) return -1;
            if (b2.getDistrict() == District.Religion) return 1;
        }
        return (b1.getCost() - b2.getCost());
    }
}
