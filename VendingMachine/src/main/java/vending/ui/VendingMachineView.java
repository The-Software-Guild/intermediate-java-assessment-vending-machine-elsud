package vending.ui;

import vending.dto.Coins;
import vending.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class VendingMachineView {

    private final UserIO io;

    @Autowired
    public VendingMachineView(UserIO io) {
        this.io = io;
    }

    /**
     * Displays inventory items which number is greater than 0 and their prices
     * @param availableItems list of Items
     */
    public void displayItems(List<Item> availableItems) {
        availableItems.stream()
                .forEach((item) -> io.print(item.getName() + " ------ $" + item.getPrice()));
    }

    /**
     * Displays menu with options to put in some amount of money or to exit
     * @return integer associated with the chosen option
     */
    public int displayMenu() {
        int choice = io.readInt(
                "\nPlease enter\n1 - to put in some money\n2 - to exit", 1, 2
        );
        return choice;
    }

    /**
     * Takes funds for purchase. Reads them as a double.
     * @return entered double
     */
    public double getMoney() {
        return io.readDouble("Please enter required amount of money");
    }

    /**
     * Displays change as amount of coins
     * @param change Map with Coins and their amount
     */
    public void displayChange(Map<Coins, Integer> change) {
        change.keySet().stream()
                        .forEach((coin) -> io.print(change.get(coin) + " " + coin.name()));
        io.readString("Please hit enter to continue");
    }

    public void displayChangeBanner() {
        io.print("Your change:");
    }

    public void displayReturnBanner() {
        io.print("Please take your money:");
    }

    /**
     * Displays banner and reads input
     * @return entered String
     */
    public String displayChoiceBanner() {
        return io.readString("Please enter the name of the selected item");
    }

    public void displayNoItemBanner(String name) {
        io.print("Item " + name + " does not available. Try again");
        io.readString("Please hit enter to continue");
    }

    public void displayInsufficientFoundBanner(String name, BigDecimal price) {
        io.print("Not enough money. " + name + " costs $" + price);
        io.readString("Please hit enter to continue");
    }

    public void displayErrorMessage(String message) {
        io.print(message);
    }

    public void displayExitMessage() {
        io.print("Good bye!");
    }

    /**
     * Displays additional menu with options to add money, reselect an item or cancel operation
     * in the case of insufficient funds
     * @param remainder the amount of money that should be added to purchase item
     * @return integer associated with the chosen option
     */
    public int displayMenuToAddMoney(BigDecimal remainder) {
        io.print("1 - Add $" + remainder);
        io.print("2 - Select another item");
        io.print("3 - Return money");
        return io.readInt("Please choose and enter the number of option:", 1, 3);
    }
}
