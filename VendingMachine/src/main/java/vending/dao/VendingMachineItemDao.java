package vending.dao;

import vending.dto.Item;

import java.util.List;

public interface VendingMachineItemDao {

    /**
     * Gets item by the name
     * @param name name of the item
     * @return Item object
     */
    public Item getItem(String name);

    /**
     * Decrement the number of items by 1
     * @param name name of the item
     */
    public void decrementItemNumber(String name);

    /**
     * Gets the list of inventory items
     * @return List of Item objects
     */
    public List<Item> getItemsList();

    /**
     * Loads items from persistent storage to the memory
     * @throws VendingMachinePersistenceException when some loading error occurs
     */
    public void loadItems() throws VendingMachinePersistenceException;

    /**
     * Uploads items from the memory to persistent storage
     * @throws VendingMachinePersistenceException when some uploading error occurs
     */
    public void uploadItems() throws VendingMachinePersistenceException;
}