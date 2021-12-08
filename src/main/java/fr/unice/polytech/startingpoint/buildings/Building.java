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

    public District getDistrict() {return building.getDistrict();}

    @Override
    public String toString() {
        return String.valueOf(building);
    }

}
