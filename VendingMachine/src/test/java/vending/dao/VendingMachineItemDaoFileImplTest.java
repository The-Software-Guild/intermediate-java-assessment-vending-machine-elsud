package vending.dao;

import vending.dao.VendingMachineItemDao;
import vending.dao.VendingMachineItemDaoFileImpl;
import vending.dao.VendingMachinePersistenceException;
import vending.dto.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VendingMachineItemDaoFileImplTest {

    VendingMachineItemDao testDao;

    private final String FIRST_NAME = "first";
    private final String SECOND_NAME = "second";
    private final BigDecimal FIRST_PRICE = new BigDecimal("3.15");
    private final BigDecimal SECOND_PRICE = new BigDecimal("4.03");
    private final int FIRST_NUMBER = 1;
    private final int SECOND_NUMBER = 0;
    private final String DELIMITER = "::";


    @BeforeEach
    public void setUp() throws IOException, VendingMachinePersistenceException {
        String testFile = "testFile.txt";
        PrintWriter out = new PrintWriter(new FileWriter(testFile));
        out.println(FIRST_NAME + DELIMITER + FIRST_PRICE + DELIMITER + FIRST_NUMBER);
        out.flush();
        out.println(SECOND_NAME + DELIMITER + SECOND_PRICE + DELIMITER + SECOND_NUMBER);
        out.flush();
        out.close();
        testDao = new VendingMachineItemDaoFileImpl(testFile);
        testDao.loadItems();
    }

    @Test
    public void testGetExistingItem() {
        Item item = testDao.getItem(FIRST_NAME);
        assertNotNull(item);
        assertEquals(FIRST_NAME, item.getName());
        assertEquals(FIRST_NUMBER, item.getNumber());
        assertEquals(FIRST_PRICE, item.getPrice());
    }

    @Test
    public void testGetNotExistingItem() {
        Item item = testDao.getItem("notExistingName");
        assertNull(item);
    }

    @Test
    public void testDecrementItemNumber() {
        testDao.decrementItemNumber(FIRST_NAME);
        Item item = testDao.getItem(FIRST_NAME);
        int newNumber = item.getNumber();
        assertNotEquals(FIRST_NUMBER, newNumber);
        assertEquals(FIRST_NUMBER-1, newNumber);
    }

    @Test
    public void testGetItemsList() {
        List<Item> itemsList = testDao.getItemsList();
        Item first = itemsList.get(0);
        Item second = itemsList.get(1);
        assertEquals(2, itemsList.size());
        assertEquals(FIRST_NAME, first.getName());
        assertEquals(FIRST_PRICE, first.getPrice());
        assertEquals(FIRST_NUMBER, first.getNumber());
        assertEquals(SECOND_NAME, second.getName());
        assertEquals(SECOND_PRICE, second.getPrice());
        assertEquals(SECOND_NUMBER, second.getNumber());
    }

    @Test
    public void testUploadItems() throws VendingMachinePersistenceException {
        Item first = testDao.getItem(FIRST_NAME);
        Item second = testDao.getItem(SECOND_NAME);
        int newFirstNumber = 13;
        int newSecondNumber = 8;
        first.setNumber(newFirstNumber);
        second.setNumber(newSecondNumber);
        testDao.uploadItems();
        testDao.loadItems();
        first = testDao.getItem(FIRST_NAME);
        second = testDao.getItem(SECOND_NAME);
        assertEquals(newFirstNumber, first.getNumber());
        assertEquals(newSecondNumber, second.getNumber());
    }

}
