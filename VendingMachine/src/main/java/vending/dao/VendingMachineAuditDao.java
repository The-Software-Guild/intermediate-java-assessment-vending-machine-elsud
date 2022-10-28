package vending.dao;

public interface VendingMachineAuditDao {

    /**
     * Writes entry to audit
     * @param entry String to write
     * @throws VendingMachinePersistenceException when some error occurs during writing
     */
    public void writeAuditEntry(String entry) throws VendingMachinePersistenceException;

}
