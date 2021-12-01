package fr.unice.polytech.startingpoint.characters;

public class Assassin extends Character{
    public Assassin(){
        super(1,"Assassin");
    }
    void power(Character victim){
        victim.setMurdered();
        System.out.println("Character "+ victim.getName() +" has been killed");
    }
    @Override
    public void usePower(){
        Character victim =player.chooseVictim();
        power(victim);
    }
}
