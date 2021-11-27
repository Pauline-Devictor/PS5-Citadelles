package fr.unice.polytech.startingpoint.characters;

public class Architect extends Character{
    public Architect(){
        super(7,"Architect");
    }

    @Override
    public void usePower(){
        //Architect allow to build 2 more buildings total =3
        getPlayer().setNbBuildable(3);
    }
}
