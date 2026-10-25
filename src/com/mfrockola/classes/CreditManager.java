package com.mfrockola.classes;

/**
 * Pure credit-domain state. UI labels and persistence deliberately remain
 * outside this class so credit behaviour can be tested without Swing.
 */
final class CreditManager {

    private int balance;
    private final int creditsPerInsert;
    private boolean freeMode;

    CreditManager(int initialBalance, int creditsPerInsert, boolean freeMode) {
        this.balance = Math.max(0, initialBalance);
        this.creditsPerInsert = Math.max(0, creditsPerInsert);
        this.freeMode = freeMode;
    }

    int balance() {
        return balance;
    }

    boolean isFreeMode() {
        return freeMode;
    }

    void setFreeMode(boolean freeMode) {
        this.freeMode = freeMode;
    }

    int addConfiguredCredits() {
        return addCredits(creditsPerInsert);
    }

    int addCredits(int amount) {
        if (amount > 0) {
            balance += amount;
        }
        return balance;
    }

    boolean canNavigate(boolean lockScreen) {
        return freeMode || balance > 0 || !lockScreen;
    }

    boolean canSelectSong() {
        return freeMode || balance > 0;
    }

    boolean consumeForSong() {
        if (freeMode) {
            return true;
        }
        if (balance <= 0) {
            return false;
        }
        balance--;
        return true;
    }

    boolean removeOneCredit() {
        if (balance <= 0) {
            return false;
        }
        balance--;
        return true;
    }
}
