package fr.unice.polytech.startingpoint;

public class Building {
    private final int cost;
    private boolean built;

    Building(int cost){
        this.cost = cost;
        built = false;
    }

    int getCost(){
        return cost;
    }

    boolean isBuildable(int gold){
        return gold >= cost && !built;
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

    @Override
    public String toString(){
        return "[nom], Cout : "+ cost + ", Construit : " + built;
    }
}
