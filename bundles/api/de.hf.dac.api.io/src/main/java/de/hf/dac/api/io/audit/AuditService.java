/**
 * ----------------------------------------------------------------------------
 * ---          HF- Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : AuditService.java
 * Author(s)   : hf
 * Created     : 04.01.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.audit;


import de.hf.dac.api.io.domain.Severity;

import java.time.LocalDateTime;

public interface AuditService {
    void saveMessage(String message, Severity severity, String messagetype, String user);
    void saveMessage(String message, Severity severity, String messagetype);
    void saveSuccessfulJournalEntry(String processStep, String arguments, String user, LocalDateTime startts, LocalDateTime endts);
    void saveFailedJournalEntry(String processStep, String arguments, String user, LocalDateTime startts, LocalDateTime endts);
}
