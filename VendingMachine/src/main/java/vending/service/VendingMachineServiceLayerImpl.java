package vending.service;

import vending.dao.VendingMachineAuditDao;
import vending.dao.VendingMachineChangeDao;
import vending.dao.VendingMachineItemDao;
import vending.dao.VendingMachinePersistenceException;
import vending.dto.Coins;
import vending.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class VendingMachineServiceLayerImpl implements VendingMachineServiceLayer {

    private final VendingMachineItemDao dao;
    private final VendingMachineAuditDao audit;
    private final VendingMachineChangeDao change = new VendingMachineChangeDao();

    private final int SCALE = 2;
    private final RoundingMode MODE = RoundingMode.HALF_UP;

    @Autowired
    public VendingMachineServiceLayerImpl(VendingMachineItemDao dao, VendingMachineAuditDao audit) {
        this.dao = dao;
        this.audit = audit;
    }

    @Override
    public List<Item> getAvailableItems() {
        return dao.getItemsList().stream()
                .filter((item) -> item.getNumber() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Coins, Integer> buyItem(String name, BigDecimal funds) throws InsufficientFundsException, VendingMachinePersistenceException {
        BigDecimal price = getPrice(name);
        int compared = price.compareTo(funds);
        switch (compared) {
            // price is higher than funds
            case 1:
                audit.writeAuditEntry(
                        "Insufficient funds: " + name + " - price $" + price + " - funds $" + funds
                        );
                throw new InsufficientFundsException("Not enough money!");
            // funds is higher than the price
            case -1:
                dao.decrementItemNumber(name);
                BigDecimal remainder = funds.subtract(price);
                audit.writeAuditEntry(
                        name + " purchased - price $" + price +
                        " - funds $" + funds + " - change $" + remainder
                        );
                return change.getChange(remainder);
        }
        // funds is equal to the price
        dao.decrementItemNumber(name);
        audit.writeAuditEntry(
            name + " purchased - price $" + price + " - funds $" + funds
        );
        return change.getEmptyChange();
    }

    @Override
    public BigDecimal convertFromDouble(double money) {
        return new BigDecimal(money).setScale(SCALE, MODE);
    }

    @Override
    public boolean checkItemAvailability(String name) throws NoItemInventoryException {
        Item item = dao.getItem(name);
        if ( item == null || item.getNumber() == 0) {
            throw new NoItemInventoryException("Item " + name + " doesn't exist");
        }
        return true;
    }

    @Override
    public BigDecimal getPrice(String name) {
        Item item = dao.getItem(name);
        return item.getPrice();
    }

    @Override
    public void loadInventory() throws VendingMachinePersistenceException {
        dao.loadItems();
    }

    @Override
    public void uploadInventory() throws VendingMachinePersistenceException {
        dao.uploadItems();
    }

    @Override
    public Map<Coins, Integer> returnMoney(BigDecimal funds) {
        return change.getChange(funds);
    }

    @Override
    public BigDecimal getFundsDifference(String name, BigDecimal funds) {
        return getPrice(name).subtract(funds);
    }
}
