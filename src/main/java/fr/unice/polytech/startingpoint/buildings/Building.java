package fr.unice.polytech.startingpoint.buildings;

public class Building {
    private final BuildingEnum building;

    public Building(BuildingEnum b) {
        building = b;
    }

    public int getCost() {
        return building.getCost();
    }

    public BuildingEnum getBuilding() {
        return building;
    }

    public String getName() {
        return building.getName();
    }

    @Override
    public String toString() {
        return String.valueOf(building);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building1 = (Building) o;
        return building == building1.building;
    }

}
