package vending.controller;

import vending.dao.VendingMachinePersistenceException;
import vending.dto.Coins;
import vending.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vending.service.InsufficientFundsException;
import vending.service.NoItemInventoryException;
import vending.service.VendingMachineServiceLayer;
import vending.ui.VendingMachineView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class VendingMachineController {

    private final VendingMachineServiceLayer service;

    private final VendingMachineView view;

    @Autowired
    public VendingMachineController(VendingMachineServiceLayer service, VendingMachineView view) {
        this.service = service;
        this.view = view;
    }

    /**
     * Runs main code to process possible requests
     * @throws NoItemInventoryException when such item doesn't exist
     */
    public void run() throws NoItemInventoryException {
        boolean ifExit = false;
        // loads inventory items to the memory, exits in case of the error
        try {
            service.loadInventory();
        } catch (VendingMachinePersistenceException e) {
            view.displayErrorMessage(e.getMessage());
            ifExit = true;
        }
        // loop to process requests
        while (!ifExit) {
            // displays items to purchase and menu
            List<Item> availableItems = service.getAvailableItems();
            view.displayItems(availableItems);
            int choice = view.displayMenu();
            // handles menu choice
            switch (choice) {
                // purchase
                case 1:
                    try {
                        startPurchase(availableItems);
                    } catch (VendingMachinePersistenceException e) {
                        view.displayErrorMessage(e.getMessage());
                        ifExit = true;
                    } finally {
                        break;
                    }
                // exit
                case 2:
                    ifExit = true;
            }
        }
        // upload inventory to persistent storage
        try {
            service.uploadInventory();
        } catch (VendingMachinePersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
        view.displayExitMessage();
    }

    /**
     * Asks user to choose an item until such item wouldn't be found available in the inventory
     * @param availableItems List of available to purchase Items
     * @return name of the chosen available item
     */
    private String chooseItem(List<Item> availableItems) {
        boolean isValid = false;
        String itemName;
        do {
            view.displayItems(availableItems);
            itemName = view.displayChoiceBanner();
            try {
                service.checkItemAvailability(itemName);
                isValid = true;
            } catch (NoItemInventoryException e) {
                view.displayNoItemBanner(itemName);
            }
        } while (!isValid);
        return itemName;
    }

    /**
     * Gets entered amount of money and coverts them to BigDecimal
     * @return entered amount of money
     */
    private BigDecimal getMoney() {
        double money = view.getMoney();
        BigDecimal converted = service.convertFromDouble(money);
        return converted;
    }

    /**
     * Tries to purchase the item. If entered amount of money is enough returns the change.
     * If the price is higher than the funds displays a banner about it.
     * @param name name of the item to purchase
     * @param funds entered amount of money
     * @return boolean was purchase successful or not
     * @throws VendingMachinePersistenceException when some error occurs during writing audit
     */
    private boolean purchase(String name, BigDecimal funds) throws VendingMachinePersistenceException {
        try {
            Map<Coins, Integer> change = service.buyItem(name, funds);
            view.displayChangeBanner();
            view.displayChange(change);
            return true;
        } catch (InsufficientFundsException e) {
            view.displayInsufficientFoundBanner(name, service.getPrice(name));
            return false;
        }
    }

    /**
     * Gets the funds and the name of the item to purchase. Purchases
     * this item, otherwise starts the process to solve the issue of insufficient funds
     * @param availableItems List of available to purchase Items
     * @throws VendingMachinePersistenceException when some error occurs during writing audit
     */
    private void startPurchase(List<Item> availableItems) throws VendingMachinePersistenceException {
        BigDecimal funds = getMoney();
        String itemName = chooseItem(availableItems);
        if (!purchase(itemName, funds)) {
            solveInsufficientFunds(itemName, funds);
        }
    }

    /**
     * Solves the insufficient funds issue. Gives several option to solve:
     * add money, reselect an item, cancel purchase operation.
     * @param name name of the item to purchase
     * @param funds enetred amount of money
     * @throws VendingMachinePersistenceException when some error occurs during writing audit
     */
    private void solveInsufficientFunds(String name, BigDecimal funds) throws VendingMachinePersistenceException {
        boolean isEnoughMoney = false;
        // loop to finish purchase process
        while (!isEnoughMoney) {
            int choice = view.displayMenuToAddMoney(service.getFundsDifference(name, funds));
            switch (choice) {
                // gets additional amount of money to purchase the same item
                case 1:
                    funds = funds.add(getMoney());
                    if (purchase(name, funds)) {
                        isEnoughMoney = true;
                    }
                    break;
                // gives an option to select another item
                case 2:
                    name = chooseItem(service.getAvailableItems());
                    if (purchase(name, funds)) {
                        isEnoughMoney = true;
                    }
                    break;
                // cancels operation and returns entered money
                case 3:
                    Map<Coins, Integer> toReturn = service.returnMoney(funds);
                    view.displayReturnBanner();
                    view.displayChange(toReturn);
                    isEnoughMoney = true;
            }
        }
    }
}
