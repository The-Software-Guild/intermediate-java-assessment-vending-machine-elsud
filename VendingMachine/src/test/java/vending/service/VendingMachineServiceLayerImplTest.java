package vending.service;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import vending.dao.VendingMachineAuditDao;
import vending.dao.VendingMachineItemDao;
import vending.dao.VendingMachinePersistenceException;
import vending.dto.Coins;
import vending.dto.Item;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class VendingMachineServiceLayerImplTest {
    VendingMachineItemDao testDao;
    VendingMachineAuditDao testAudit;
    VendingMachineServiceLayer service;

    public VendingMachineServiceLayerImplTest() {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan(VendingMachineAuditDaoStubImpl.class.getPackageName());
        appContext.refresh();
        testDao = appContext.getBean(
                "vendingMachineItemDaoStubImpl", VendingMachineItemDaoStubImpl.class
        );
        testAudit = appContext.getBean(
                "vendingMachineAuditDaoStubImpl", VendingMachineAuditDaoStubImpl.class
        );
        //service = new VendingMachineServiceLayerImpl(testDao, testAudit);
        appContext.register(VendingMachineServiceLayerImpl.class);
        service = appContext.getBean(
                "vendingMachineServiceLayerImpl", VendingMachineServiceLayerImpl.class
        );
    }

    private final String FIRST_NAME = "first";
    private final String SECOND_NAME = "second";
    private final BigDecimal FIRST_PRICE = new BigDecimal("4.03");
    private final BigDecimal SECOND_PRICE = new BigDecimal("2.67");
    private final int FIRST_NUMBER = 0;
    private final int SECOND_NUMBER = 5;

    @Test
    public void testGetAvailableItems() {
        List<Item> itemsList = service.getAvailableItems();
        Item item = itemsList.get(0);
        assertEquals(1, itemsList.size());
        assertEquals(SECOND_NUMBER, item.getNumber());
        assertEquals(SECOND_NAME, item.getName());
        assertEquals(SECOND_PRICE, item.getPrice());
    }

    @Test
    public void testBuyItemWithInsufficientFunds() {
        assertThrows(
                InsufficientFundsException.class, () -> service.buyItem(
                        SECOND_NAME, new BigDecimal("0.0")
                )
        );
        int numberAfter = testDao.getItem(SECOND_NAME).getNumber();
        assertEquals(SECOND_NUMBER, numberAfter);
    }

    @Test
    public void testBuyItemWithExactlyRequiredFunds() throws VendingMachinePersistenceException, InsufficientFundsException {
        Map<Coins, Integer> change = service.buyItem(SECOND_NAME, SECOND_PRICE);int  numberAfter = testDao.getItem(SECOND_NAME).getNumber();
        assertEquals(SECOND_NUMBER-1, numberAfter);
        assertEquals(Optional.of(0), Optional.ofNullable(change.get(Coins.QUARTERS)));
        assertEquals(Optional.of(0), Optional.ofNullable(change.get(Coins.DIMES)));
        assertEquals(Optional.of(0), Optional.ofNullable(change.get(Coins.NICKELS)));
        assertEquals(Optional.of(0), Optional.ofNullable(change.get(Coins.PENNIES)));
    }

    @Test
    public void testBuyItemWithChange() throws VendingMachinePersistenceException, InsufficientFundsException {
        Map<Coins, Integer> change = service.buyItem(SECOND_NAME, SECOND_PRICE.add(new BigDecimal("0.16")));
        int  numberAfter = testDao.getItem(SECOND_NAME).getNumber();
        assertEquals(SECOND_NUMBER-1, numberAfter);
        assertEquals(Optional.of(0), Optional.ofNullable(change.get(Coins.QUARTERS)));
        assertEquals(Optional.of(1), Optional.ofNullable(change.get(Coins.DIMES)));
        assertEquals(Optional.of(1), Optional.ofNullable(change.get(Coins.NICKELS)));
        assertEquals(Optional.of(1), Optional.ofNullable(change.get(Coins.PENNIES)));
    }

    @Test
    public void testConvertFromDoubleWithHalfUpMode() {
        BigDecimal converted = service.convertFromDouble(1.117);
        assertEquals(new BigDecimal("1.12"), converted);
    }

    @Test
    public void testConvertFromDoubleWithScale2() {
        BigDecimal converted = service.convertFromDouble(1.0);
        assertEquals(new BigDecimal("1.00"), converted);
    }

    @Test
    public void testItemAvailabilityForAvailableItem() throws NoItemInventoryException {
        boolean isAvailable = service.checkItemAvailability(SECOND_NAME);
        assertTrue(isAvailable);
    }

    @Test
    public void testItemAvailabilityForNotExistingItem() throws NoItemInventoryException {
        assertThrows(NoItemInventoryException.class, () -> service.checkItemAvailability("notExisting"));
    }

    @Test
    public void testItemAvailabilityForNotAvailableItem() throws NoItemInventoryException {
        assertThrows(NoItemInventoryException.class, () -> service.checkItemAvailability(FIRST_NAME));
    }

    @Test
    public void testGetPrice() {
        assertEquals(FIRST_PRICE, service.getPrice(FIRST_NAME));
    }

    @Test
    public void testGetFundsDifferenceForEqualFunds() {
        BigDecimal zeroDifference = service.getFundsDifference(FIRST_NAME, FIRST_PRICE);
        assertEquals(new BigDecimal("0.00"), zeroDifference);
    }

    @Test
    public void testGetFundsDifferenceForDifferentFunds() {
        BigDecimal returnedDifference = service.getFundsDifference(FIRST_NAME, SECOND_PRICE);
        assertEquals(FIRST_PRICE, returnedDifference.add(SECOND_PRICE));
    }

    @Test
    public void testReturnMoney() {
        Map<Coins, Integer> change = service.returnMoney(new BigDecimal("0.16"));
        assertEquals(Optional.of(0), Optional.ofNullable(change.get(Coins.QUARTERS)));
        assertEquals(Optional.of(1), Optional.ofNullable(change.get(Coins.DIMES)));
        assertEquals(Optional.of(1), Optional.ofNullable(change.get(Coins.NICKELS)));
        assertEquals(Optional.of(1), Optional.ofNullable(change.get(Coins.PENNIES)));
    }
}
