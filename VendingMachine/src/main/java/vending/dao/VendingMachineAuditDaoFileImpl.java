package vending.dao;

import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@Component
public class VendingMachineAuditDaoFileImpl implements VendingMachineAuditDao {

    private final String AUDIT_FILE = "audit.txt";

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
