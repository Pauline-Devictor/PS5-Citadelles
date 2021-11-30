package fr.unice.polytech.startingpoint;



public class Building {
    private final BuildingEnum building;
    private boolean built;

    Building(BuildingEnum b) {
        building = b;
        built = false;
    }

    void build() {
        built = true;
    }

    void destroy() {
        built = false;
    }

    boolean getBuilt() {
        return built;
    }

    int getCost() {
        return building.getCost();
    }

    public BuildingEnum getBuilding() {
        return building;
    }

    String getName() {
        return building.getName();
    }

    @Override
    public String toString() {
        return building + ", Construit : " + getBuilt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building1 = (Building) o;
        return building == building1.building;
    }
}
