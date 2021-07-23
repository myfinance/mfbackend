package de.hf.myfinance.test.mock;

import java.time.LocalDateTime;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;

public class AuditServiceMockImpl implements AuditService {

    @Override
    public void saveMessage(String message, Severity severity, String messagetype, String user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveMessage(String message, Severity severity, String messagetype) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveSuccessfulJournalEntry(String processStep, String arguments, String user, LocalDateTime startts,
            LocalDateTime endts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveFailedJournalEntry(String processStep, String arguments, String user, LocalDateTime startts,
            LocalDateTime endts) {
        // TODO Auto-generated method stub

    }
    
}