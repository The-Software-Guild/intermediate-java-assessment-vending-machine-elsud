package vending.service;

import vending.dao.VendingMachinePersistenceException;
import vending.dto.Coins;
import vending.dto.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface VendingMachineServiceLayer {

    /**
     * Gets inventory items which number is greater than 0
     * @return List of Items
     */
    public List<Item> getAvailableItems();

    /**
     * Tries to purchase an item. In case of efficient purchase returns change and
     * decrements number of items. In case of insufficient funds throws InsufficientFundsException.
     * Writes audit for each operation.
     * @param name name of Item to purchase
     * @param funds entered amount of money
     * @return Map with Coins and their amount
     * @throws InsufficientFundsException when funds are less than price
     * @throws VendingMachinePersistenceException when some error occurs during writing audit
     */
    public Map<Coins, Integer> buyItem(String name, BigDecimal funds) throws InsufficientFundsException, VendingMachinePersistenceException;

    /**
     * Converts double to BigDecimal
     * @param funds entered amount of money
     * @return entered amount as a BigDecimal
     */
    public BigDecimal convertFromDouble(double funds);

    /**
     * Checks if item exists in the inventory and its number is greater than 0
     * @param name name of the item
     * @return boolean
     * @throws NoItemInventoryException when item doesn't exist or there are 0 items in the inventory
     */
    public boolean checkItemAvailability(String name) throws NoItemInventoryException;

    /**
     * Gets price of the item
     * @param name name of the item
     * @return item's price
     */
    public BigDecimal getPrice(String name);

    /**
     * Loads inventory items to the memory
     * @throws VendingMachinePersistenceException when some error occurs during loading
     */
    public void loadInventory() throws VendingMachinePersistenceException;

    /**
     * Upoads inventory items from the memory to the persistent storage
     * @throws VendingMachinePersistenceException when some error occurs during uploading
     */
    public void uploadInventory() throws VendingMachinePersistenceException;

    /**
     * Modifies entered amount of money to amount of coins to return
     * @param funds amount of money to return
     * @return Map with Coins and their amount
     */
    public Map<Coins, Integer> returnMoney(BigDecimal funds);

    /**
     * Calculates the difference between entered funds and the price of the item
     * @param name name of the item
     * @param funds entered amount of money
     * @return amount of money that should be entered to purchase this item
     */
    public BigDecimal getFundsDifference(String name, BigDecimal funds);

}