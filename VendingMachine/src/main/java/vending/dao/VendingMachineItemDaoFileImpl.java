package vending.dao;

import vending.dto.Item;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class VendingMachineItemDaoFileImpl implements VendingMachineItemDao {

    // Map for storing inventory items
    private Map<String, Item> inventoryItems = new HashMap<>();
    private final String DELIMITER = "::";
    private final String FILE_NAME;

    public VendingMachineItemDaoFileImpl() {
        FILE_NAME = "inventory.txt";
    }

    public VendingMachineItemDaoFileImpl(String fileName) {
        FILE_NAME = fileName;
    }

    @Override
    public Item getItem(String name) {
        return inventoryItems.get(name);
    }

    @Override
    public void decrementItemNumber(String name) {
        Item item = inventoryItems.get(name);
        item.setNumber(item.getNumber() - 1);
    }

    @Override
    public List<Item> getItemsList() {
        return new ArrayList<>(inventoryItems.values());
    }

    /**
     * Unmarshalls String with item properties to a new Item object
     * @param itemAsString
     * @return Item object
     */
    private Item unmarshallItem(String itemAsString) {
        return new Item(itemAsString.split(DELIMITER));
    }

    /**
     * Reads items as strings from the file, unmarshalls them to the
     * Item objects and storing to inventoryItems Map
     * @throws VendingMachinePersistenceException when inventory file doesn't exist
     */
    @Override
    public void loadItems() throws VendingMachinePersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(FILE_NAME)));
        } catch (FileNotFoundException e) {
            throw new VendingMachinePersistenceException("-_- Could not load Items data into memory.", e);
        }
        String currentLine;
        Item currentItem;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentItem = unmarshallItem(currentLine);
            inventoryItems.put(currentItem.getName(), currentItem);
        }
        scanner.close();
    }

    /**
     * Marshalls an item to a String
     * @param item Item object
     * @return String with item properties separated with DELIMITER
     */
    private String marshallItem(Item item) {
        return item.getName() + DELIMITER + item.getPrice() + DELIMITER + item.getNumber();
    }

    /**
     * Marshalls each item to a String and writes them to the file line by line
     * @throws VendingMachinePersistenceException when IOException occurs
     */
    @Override
    public void uploadItems() throws VendingMachinePersistenceException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(FILE_NAME));
        } catch (IOException e) {
            throw new VendingMachinePersistenceException("Could not save Items data", e);
        }
        String itemAsString;
        for (Item item : inventoryItems.values()) {
            itemAsString = marshallItem(item);
            out.println(itemAsString);
            out.flush();
        }
        out.close();
    }
}
