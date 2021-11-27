package fr.unice.polytech.startingpoint.characters;

public class Assassin extends Character{
    public Assassin(){
        super(1,"Assassin");
    }
    @Override
    public void usePower(){
        Character c =player.chooseVictim();
        c.setMurdered();
        System.out.println("Character "+ c.getName() +" has been killed");
    }
}
