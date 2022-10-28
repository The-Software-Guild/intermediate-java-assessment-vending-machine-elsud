package vending.dao;

import vending.dao.VendingMachineChangeDao;
import vending.dto.Coins;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VendingMachineChangeDaoTest {

    VendingMachineChangeDao change = new VendingMachineChangeDao();

    @Test
    public void testGetChangeForZeroFunds() {
        Map<Coins, Integer> coins = change.getChange(new BigDecimal("0.0"));
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.QUARTERS)));
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.DIMES)));
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.NICKELS)));
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.PENNIES)));
    }

    @Test
    public void testGetChangeFor3PennyFunds() {
        Map<Coins, Integer> coins = change.getChange(new BigDecimal("0.03"));
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.QUARTERS)));
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.DIMES)));
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.NICKELS)));
        assertEquals(Optional.of(3), Optional.ofNullable(coins.get(Coins.PENNIES)));
    }

    @Test
    public void testGetChangeFor5PennyFunds() {
        Map<Coins, Integer> coins = change.getChange(new BigDecimal("0.05"));
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.QUARTERS)));
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.DIMES)));
        assertEquals(Optional.of(1), Optional.ofNullable(coins.get(Coins.NICKELS)));
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.PENNIES)));
    }

    @Test
    public void testGetChangeWith1OfEachCoin() {
        Map<Coins, Integer> coins = change.getChange(new BigDecimal("0.41"));
        assertEquals(Optional.of(1), Optional.ofNullable(coins.get(Coins.QUARTERS)));
        assertEquals(Optional.of(1), Optional.ofNullable(coins.get(Coins.DIMES)));
        assertEquals(Optional.of(1), Optional.ofNullable(coins.get(Coins.NICKELS)));
        assertEquals(Optional.of(1), Optional.ofNullable(coins.get(Coins.PENNIES)));
    }

    @Test
    public void testGetEmptyChange() {
        Map<Coins, Integer> coins = change.getEmptyChange();
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.QUARTERS)));
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.DIMES)));
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.NICKELS)));
        assertEquals(Optional.of(0), Optional.ofNullable(coins.get(Coins.PENNIES)));
    }
}
