package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class VendingMachineAuditDaoFileImpl implements VendingMachineAuditDao {

    private final String AUDIT_FILE;

    public VendingMachineAuditDaoFileImpl() {
        AUDIT_FILE = "audit.txt";
    }

    public VendingMachineAuditDaoFileImpl(String auditFile) {
        AUDIT_FILE = auditFile;
    }

    @Override
    public void writeAuditEntry(String entry) throws VendingMachinePersistenceException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new VendingMachinePersistenceException("Could not persist audit information.", e);
        }
        LocalDateTime timestamp = LocalDateTime.now();
        out.println(timestamp + " - " + entry);
        out.flush();
        out.close();
    }
}