import controller.VendingMachineController;
import dao.*;
import service.NoItemInventoryException;
import service.VendingMachineServiceLayer;
import service.VendingMachineServiceLayerImpl;
import ui.UserIOConsoleImpl;
import ui.VendingMachineView;

public class App {

    public static void main(String[] args) throws NoItemInventoryException {
        VendingMachineView view = new VendingMachineView(new UserIOConsoleImpl());
        VendingMachineItemDao dao = new VendingMachineItemDaoFileImpl();
        VendingMachineAuditDao audit = new VendingMachineAuditDaoFileImpl();
        VendingMachineServiceLayer service = new VendingMachineServiceLayerImpl(dao, audit);
        VendingMachineController controller = new VendingMachineController(service, view);
        controller.run();
    }
}
