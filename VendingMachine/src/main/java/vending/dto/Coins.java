package vending.dto;

import java.math.BigDecimal;

public enum Coins {
    QUARTERS(new BigDecimal("0.25")),
    DIMES(new BigDecimal("0.1")),
    NICKELS(new BigDecimal("0.05")),
    PENNIES(new BigDecimal("0.01"));

    public final BigDecimal value;

    private Coins(BigDecimal value) {
        this.value = value;
    }
}
