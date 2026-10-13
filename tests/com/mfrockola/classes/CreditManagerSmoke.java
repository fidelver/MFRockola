package com.mfrockola.classes;

public final class CreditManagerSmoke {
    public static void main(String[] args) {
        CreditManager credits = new CreditManager(0, 2, false);

        check(credits.balance() == 0, "initial balance");
        check(!credits.canNavigate(true), "locked navigation without credits");
        check(credits.canNavigate(false), "unlocked navigation without credits");
        check(!credits.canSelectSong(), "cannot select paid song without credit");

        credits.addConfiguredCredits();
        check(credits.balance() == 2, "configured insert adds two credits");
        check(credits.canNavigate(true), "credit unlocks navigation");
        check(credits.consumeForSong(), "first paid song consumes");
        check(credits.balance() == 1, "balance after first song");
        check(credits.consumeForSong(), "second paid song consumes");
        check(credits.balance() == 0, "balance after second song");
        check(!credits.consumeForSong(), "cannot over-consume");

        credits.setFreeMode(true);
        check(credits.canNavigate(true), "free mode bypasses lockScreen credit gate");
        check(credits.canSelectSong(), "free mode can select song");
        check(credits.consumeForSong(), "free mode accepts song");
        check(credits.balance() == 0, "free mode does not consume balance");

        credits.setFreeMode(false);
        credits.addCredits(3);
        check(credits.removeOneCredit(), "manual removal");
        check(credits.balance() == 2, "manual removal balance");

        System.out.println("MFR_CREDIT_MANAGER_OK");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
