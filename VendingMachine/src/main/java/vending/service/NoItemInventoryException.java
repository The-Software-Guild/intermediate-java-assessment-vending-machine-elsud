package vending.service;

public class NoItemInventoryException extends Exception {
    public NoItemInventoryException(String message) {
        super(message);
    }

}
