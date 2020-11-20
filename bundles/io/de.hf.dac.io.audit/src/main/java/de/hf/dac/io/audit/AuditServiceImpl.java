/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : AuditServiceImpl.java
 * Author(s)   : hf
 * Created     : 04.01.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.audit;

import java.time.LocalDateTime;
import java.util.Date;

import javax.inject.Inject;

import de.hf.dac.api.io.audit.AuditDao;
import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.DacJournal;
import de.hf.dac.api.io.domain.DacMessages;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.api.io.domain.Status;

public class AuditServiceImpl implements AuditService {

    private AuditDao auditDao;

    @Inject
    public AuditServiceImpl(AuditDao auditDao){
        this.auditDao = auditDao;
    }

    @Override
    public void saveMessage(String message, Severity severity, String messagetype, String user) {
        DacMessages msg = new DacMessages(message, severity, LocalDateTime.now());
        msg.setDacMessagetype(messagetype);
        msg.setUser(user);
        auditDao.saveMessage(msg);
    }

    @Override
    public void saveMessage(String message, Severity severity, String messagetype) {
        saveMessage(message, severity, messagetype, "NA");
    }

    @Override
    public void saveSuccessfulJournalEntry(String processStep, String arguments, String user, LocalDateTime startts, LocalDateTime endts) {
        DacJournal entry = new DacJournal(100, Status.COMPLETED.toString(), processStep, arguments, startts, endts, endts, user);
        auditDao.saveJournalEntry(entry);
    }

    @Override
    public void saveFailedJournalEntry(String processStep, String arguments, String user, LocalDateTime startts, LocalDateTime endts) {
        DacJournal entry = new DacJournal(100, Status.FAILED.toString(), processStep, arguments, startts, endts, endts, user);
        auditDao.saveJournalEntry(entry);
    }
}
