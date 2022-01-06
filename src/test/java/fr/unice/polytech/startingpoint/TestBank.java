package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestBank {
    @Test
    void withDrawGold1() {
        Bank b = new Bank(30);
        assertEquals(3, b.withdrawGold(3));
    }

    @Test
    void withDrawGold2() {
        Bank b = new Bank(30);
        assertEquals(30, b.withdrawGold(32));
    }

    @Test
    void withDrawGold3() {
        Bank b = new Bank(30);
        assertEquals(3, b.withdrawGold(-3));
    }

    @Test
    void refundGold1() {
        Bank b = new Bank(30);
        assertThrows(RuntimeException.class, () -> b.refundGold(3));
    }

    @Test
    void refundGold2() {
        Bank b = new Bank(30);
        b.refundGold(-3);
        assertEquals(b.getGold(), 30);
    }
}
