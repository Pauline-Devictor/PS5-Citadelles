package fr.unice.polytech.startingpoint;

public class Bank {
    private final int bankLimit;
    private int currentAmount;

    Bank(int bankLimit){
        this.bankLimit = bankLimit;
        currentAmount = bankLimit;
    }

    int withdrawGold(int gold){
        if(currentAmount < gold){
            currentAmount = 0;
            return currentAmount;
        }
        currentAmount -= gold;
        return gold;
    }

    void refundGold(int gold){
        if(currentAmount+gold > bankLimit){
            throw new RuntimeException("bank is richer than it's supposed to be");
        }
        currentAmount+=gold;

    }

    int getGold(){
        return currentAmount;
    }


}
