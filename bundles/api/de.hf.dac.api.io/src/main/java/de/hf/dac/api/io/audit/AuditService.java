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

public interface AuditService {
    void saveMessage(String message, Severity severity, String messagetype, String user);
    void saveMessage(String message, Severity severity, String messagetype);
}
