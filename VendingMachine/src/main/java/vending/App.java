package vending;

import vending.controller.VendingMachineController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import vending.service.NoItemInventoryException;

public class App {

    public static void main(String[] args) throws NoItemInventoryException {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("vending");
        appContext.refresh();
        VendingMachineController controller = appContext.getBean(
                "vendingMachineController", VendingMachineController.class
        );
        controller.run();
    }
}
