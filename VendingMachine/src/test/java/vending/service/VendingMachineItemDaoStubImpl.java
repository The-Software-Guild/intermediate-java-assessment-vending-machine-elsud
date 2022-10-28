package vending.service;

import org.springframework.stereotype.Component;
import vending.dao.VendingMachineItemDao;
import vending.dao.VendingMachinePersistenceException;
import vending.dto.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VendingMachineItemDaoStubImpl implements VendingMachineItemDao {

    private Map<String, Item> inventoryItems = new HashMap<>();

    private final String FIRST_NAME = "first";
    private final String SECOND_NAME = "second";
    private final BigDecimal FIRST_PRICE = new BigDecimal("4.03");
    private final BigDecimal SECOND_PRICE = new BigDecimal("2.67");
    private final int FIRST_NUMBER = 0;
    private final int SECOND_NUMBER = 5;

    public VendingMachineItemDaoStubImpl() {
        inventoryItems.put(FIRST_NAME, new Item(FIRST_NAME, FIRST_PRICE, FIRST_NUMBER));
        inventoryItems.put(SECOND_NAME, new Item(SECOND_NAME, SECOND_PRICE, SECOND_NUMBER));
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

    @Override
    public void loadItems() throws VendingMachinePersistenceException {
        // do nothing
    }

    @Override
    public void uploadItems() throws VendingMachinePersistenceException {
        // do nothing
    }
}
