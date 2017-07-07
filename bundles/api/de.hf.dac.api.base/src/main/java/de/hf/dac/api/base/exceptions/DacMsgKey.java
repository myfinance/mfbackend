/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DacMsgKey.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 05.07.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.base.exceptions;

public enum DacMsgKey implements MsgKey {
    // 10000 - 14999: system errors
    SYSTEM(10001),
    NULL_POINTER(10003),
    ILLEGAL_ARGUMENTS(10004),
    INPUT_OUTPUT(10005),
    FILE_NOT_FOUND(10100),
    PROCESS_KILLED_BY_EVENT_EXCEPTION(10105),

    INTERRUPTED_EXCEPTION(10101),

    EXECUTION_EXCEPTION(10102),

    // 15000 - 19999: hibernate, database, sybase
    // 20000 - 20999: memory, caching, couchbase
    UNSPECIFIED(21000);

    private final String globalPrefix = "DAC";

    private final int id;

    DacMsgKey(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getMsgPrefix() {
        if (id != 1) {
            return globalPrefix + "-" + getId() + ": ";
        }
        return globalPrefix + "-" + getId() + ": System error - ";
    }

    public String getName() {
        return toString();
    }
}

