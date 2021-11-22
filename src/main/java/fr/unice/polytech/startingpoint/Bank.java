package fr.unice.polytech.startingpoint;

public class Bank {
    private final int bankLimit;
    private int currentAmount;

    Bank(int bankLimit){
        this.bankLimit = bankLimit;
        currentAmount = bankLimit;
    }

    int withdrawGold(int gold){
        if(currentAmount < Math.abs(gold)){
            currentAmount = 0;
            return currentAmount;
        }
        currentAmount -= Math.abs(gold);
        return Math.abs(gold);
    }

    void refundGold(int gold) {
        if(currentAmount+Math.abs(gold) > bankLimit){
            throw new RuntimeException("The bank is richer than it should be.");
        }
        currentAmount+=Math.abs(gold);
    }

    int getGold(){
        return currentAmount;
    }
}
