package fr.unice.polytech.startingpoint;

public class Bank {
    private final int bankLimit;
    private int currentAmount;

    Bank(int bankLimit) {
        this.bankLimit = bankLimit;
        currentAmount = bankLimit;
    }

    public int withdrawGold(int gold) {
        gold = Math.abs(gold);
        int res = Math.abs(gold);
        if (currentAmount < gold) {
            res = currentAmount;
            currentAmount = 0;
        } else
            currentAmount -= gold;
        return res;
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
}
