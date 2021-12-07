package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.strategies.Player;

public class Bank {
    private final int bankLimit;
    private int currentAmount;

    Bank(int bankLimit) {
        this.bankLimit = bankLimit;
        currentAmount = bankLimit;
    }

    public int withdrawGold(int gold){
        gold = Math.abs(gold);
        if(currentAmount < gold){
            currentAmount = 0;
            return currentAmount;
        }
        currentAmount -= gold;
        return Math.abs(gold);
    }

    public int refundGold(int gold) {
        gold = Math.abs(gold);
        if (currentAmount + gold > bankLimit) {
            throw new IllegalArgumentException("The bank is richer than it should be.");
        }
        currentAmount += gold;
        return Math.abs(gold);
    }

    public int getGold() {
        return currentAmount;
    }

    public boolean isEmpty() {
        return currentAmount == 0;
    }

    /*public void transferGold(int amount, Player receiver) {
        if (bankLimit - amount < 0) {
            currentAmount += amount;
            receiver.takeMoney(amount);
            currentAmount -= amount;
        } else {
            receiver.takeMoney(amount);
        }
    }*/
}
