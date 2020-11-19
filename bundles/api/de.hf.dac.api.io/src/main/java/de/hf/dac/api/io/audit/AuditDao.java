/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : AuditDao.java
 * Author(s)   : hf
 * Created     : 03.01.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.audit;

import java.util.List;

import de.hf.dac.api.io.domain.DacJournal;
import de.hf.dac.api.io.domain.DacMessages;

public interface AuditDao {
    public static final String ADB = "adb";

    void saveMessage(DacMessages message);
    List<DacMessages> getAllMessages();

    void saveJournalEntry(DacJournal journal);
    List<DacJournal> getAllJournalEntries();
}
