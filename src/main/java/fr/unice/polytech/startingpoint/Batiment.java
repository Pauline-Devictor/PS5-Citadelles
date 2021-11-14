package fr.unice.polytech.startingpoint;

public class Batiment {
    private final int cout;
    private boolean construit;

    Batiment(int cout){
        this.cout = cout;
        construit = false;
    }

    int getCout(){
        return cout;
    }

    boolean isConstructible(int or){
        if(or >= cout) return true;
        return false;
    }

    void setConstruit(boolean b){
        construit = b;
    }
    boolean getConstruit(){return construit;}

    @Override
    public String toString(){
        return "Batiment : [nom], Cout : "+cout + ", Construit : " + construit;
    }
}
