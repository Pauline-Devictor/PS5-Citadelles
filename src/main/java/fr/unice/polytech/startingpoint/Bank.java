package fr.unice.polytech.startingpoint;

/**
 * The type Bank.
 */
public class Bank {
    private final int bankLimit;
    private int currentAmount;

    /**
     * Instantiates a new Bank.
     *
     * @param bankLimit the bank limit
     */
    public Bank(int bankLimit) {
        this.bankLimit = bankLimit;
        currentAmount = bankLimit;
    }

    /**
     * Withdraw gold
     *
     * @param gold the gold
     * @return the amount withdrew
     */
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

    /**
     * Refund gold.
     *
     * @param gold the gold
     * @return the amount refunded
     */
    public int refundGold(int gold) {
        gold = Math.max(gold, 0);
        if (currentAmount + gold > bankLimit) {
            throw new IllegalArgumentException("The bank is richer than it should be.");
        }
        currentAmount += gold;
        return Math.abs(gold);
    }

    /**
     * Gets gold.
     *
     * @return the gold
     */
    public int getGold() {
        return currentAmount;
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return currentAmount == 0;
    }
}
