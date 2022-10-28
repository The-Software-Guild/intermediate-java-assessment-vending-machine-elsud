package vending.dto;

import java.math.BigDecimal;

public class Item {

    private String name;
    private BigDecimal price;
    private int number;

    public Item(String name, BigDecimal price, int number) {
        this.name = name;
        this.price = price;
        this.number = number;
    }

    public Item(String[] array) {
        this.name = array[0];
        this.price = new BigDecimal(array[1]);
        this.number = Integer.parseInt(array[2]);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
