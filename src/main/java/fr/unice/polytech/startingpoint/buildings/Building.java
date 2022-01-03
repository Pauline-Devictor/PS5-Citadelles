package fr.unice.polytech.startingpoint.buildings;


/**
 * The type Building.
 */
public class Building {
    private final BuildingEnum building;

    /**
     * Instantiates a new Building.
     *
     * @param b the Building
     */
    public Building(BuildingEnum b) {
        building = b;
    }

    /**
     * Gets cost.
     *
     * @return the cost
     */
    public int getCost() {
        return building.getCost();
    }

    /**
     * Gets building.
     *
     * @return the building
     */
    public BuildingEnum getBuilding() {
        return building;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return building.getName();
    }

    /**
     * Gets district.
     *
     * @return the district
     */
    public District getDistrict() {
        return building.getDistrict();
    }

    @Override
    public String toString() {
        return String.valueOf(building);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Building) {
            return building.equals(((Building) o).getBuilding());
        }
        return false;
    }
}
