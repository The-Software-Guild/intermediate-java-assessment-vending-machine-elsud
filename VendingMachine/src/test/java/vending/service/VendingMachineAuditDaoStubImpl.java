package vending.service;

import org.springframework.stereotype.Component;
import vending.dao.VendingMachineAuditDao;
import vending.dao.VendingMachinePersistenceException;

@Component
public class VendingMachineAuditDaoStubImpl implements VendingMachineAuditDao {
    @Override
    public void writeAuditEntry(String entry) throws VendingMachinePersistenceException {
        // do nothing
    }
}
