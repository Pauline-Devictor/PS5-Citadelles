package fr.unice.polytech.startingpoint;

public class Building {
    private final BuildingEnum building;
    private boolean built;

    Building(BuildingEnum b) {
        building = b;
        built=false;
    }

    void setBuilt(boolean b){
        built = b;
    }

    void build(){
        built=true;
    }

    void destroy(){
        built=false;
    }

    boolean getBuilt(){return built;}

    int getCost(){
        return building.getCost();
    }

    public BuildingEnum getBuilding() {
        return building;
    }

    public boolean isBuilt() {
        return built;
    }

    @Override
    public String toString() {
        return building + ", Construit : "+ getBuilt();
    }
}
