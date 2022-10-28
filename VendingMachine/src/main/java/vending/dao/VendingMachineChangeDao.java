package vending.dao;

import vending.dto.Coins;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class VendingMachineChangeDao {

    // Map for storing change
    private Map<Coins, Integer> coins = new HashMap<>();

    /**
     * Calculates the number of coins of each type to reproduce given amount
     * of money as a number of coins
      * @param funds amount of money
     * @return Map with Coins and their amount
     */
    public Map<Coins, Integer> getChange(BigDecimal funds) {
            BigDecimal remainder;
            remainder = calculateCoinsAndRemainder(funds, Coins.QUARTERS);
            remainder = calculateCoinsAndRemainder(remainder, Coins.DIMES);
            remainder = calculateCoinsAndRemainder(remainder, Coins.NICKELS);
            coins.put(Coins.PENNIES, remainder.multiply(BigDecimal.valueOf(100)).intValue());
            return coins;
        }

    /**
     * Calculates number of coins that can reproduce this funds and the rest of the funds
     * that cannot be reproduced with this kind of the coins
     * @param funds amount of money
     * @param coin Coins enum constant
     * @return the rest of the money
     */
        private BigDecimal calculateCoinsAndRemainder(BigDecimal funds, Coins coin) {
            BigDecimal[] result =  funds.divideAndRemainder(coin.value);
            int numberOfCoins = result[0].intValue();
            coins.put(coin, numberOfCoins);
            return result[1];
        }

    /**
     * Generates Map for change with 0 amount of coins
     * @return Map with Coins and zeros
     */
    public Map<Coins, Integer> getEmptyChange() {
            for (Coins coin: Coins.values()) {
                coins.put(coin, 0);
            }
            return coins;
        }
}
